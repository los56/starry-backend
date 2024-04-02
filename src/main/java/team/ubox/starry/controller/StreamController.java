package team.ubox.starry.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.ubox.starry.dto.stream.ResponseStreamDTO;
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
            throw new IllegalStateException("잘못된 키");
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
            throw new IllegalStateException("잘못된 키");
        }

        Boolean result = streamService.endPublish(key);

        return ResponseEntity.ok(true);
    }

    @GetMapping("/live")
    public ResponseEntity<ResponseStreamDTO> live(@RequestParam(name="channel") String channelId) {
        return ResponseEntity.ok(streamService.stream(channelId));
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
