package team.ubox.starry.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.ubox.starry.service.dto.StarryResponse;
import team.ubox.starry.service.dto.stream.ResponseStreamDTO;
import team.ubox.starry.exception.StarryError;
import team.ubox.starry.exception.StarryException;
import team.ubox.starry.service.StreamService;

@RestController
@RequestMapping("/api/stream")
public class StreamController {
    private final StreamService streamService;

    @Value("${starry.mediaserver.url}")
    String mediaServerURL;

    @Autowired
    public StreamController(StreamService streamService) {
        this.streamService = streamService;
    }

    @PostMapping("/on-publish")
    public ResponseEntity<String> onPublish(@RequestBody String body) {
        String key = getKeyFromBody(body);
        if(key == null) {
            throw new StarryException(StarryError.INVALID_STREAM_KEY);
        }

        String streamId = streamService.requestPublish(key);
        if(streamId == null) {
            return ResponseEntity.status(401).build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", mediaServerURL + "/" + streamId);

        return ResponseEntity.status(302).headers(headers).body(streamId);
    }

    @PostMapping("/on-publish-done")
    public ResponseEntity<Boolean> onPublishDone(@RequestBody String body) {
        String key = getKeyFromBody(body);
        if(key == null) {
            throw new StarryException(StarryError.INVALID_STREAM_KEY);
        }
        streamService.endPublish(key);

        return ResponseEntity.ok(true);
    }

    @GetMapping("/live")
    public StarryResponse<ResponseStreamDTO> getSteamDetail(@RequestParam(name="channel") String channelId) {
        return new StarryResponse<>(streamService.stream(channelId));
    }

    private String getKeyFromBody(String body) {
        String[] andSplit = body.split("&");
        for(String str : andSplit) {
            String[] temp = str.split("=");
            if(temp[0].equals("name"))
                return temp[1];
        }
        return null;
    }
}
