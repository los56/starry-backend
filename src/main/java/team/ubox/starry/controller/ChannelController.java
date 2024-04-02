package team.ubox.starry.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.ubox.starry.dto.channel.ResponseChannelDTO;
import team.ubox.starry.dto.channel.ResponseStreamKeyDTO;
import team.ubox.starry.dto.stream.RequestChangeStreamInfoDTO;
import team.ubox.starry.dto.channel.ResponseStreamInfoDTO;
import team.ubox.starry.dto.stream.ResponseStreamDTO;
import team.ubox.starry.service.ChannelService;

@RestController
@RequestMapping("/api/channel")
@RequiredArgsConstructor
public class ChannelController {
    private final ChannelService channelService;

    @PostMapping("/open")
    public ResponseEntity<ResponseChannelDTO> open() {
        return ResponseEntity.ok(channelService.open());
    }

    @GetMapping("")
    public ResponseEntity<ResponseChannelDTO> channelData(@RequestParam String id) {
        if(id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Require channel id");
        }
        return ResponseEntity.ok(channelService.channelData(id));
    }

    @GetMapping("/studio/regen-key")
    public ResponseEntity<ResponseStreamKeyDTO> generateStreamKey() {
        return ResponseEntity.ok(channelService.generateStreamKey());
    }

    @GetMapping("/studio/stream-info")
    public ResponseEntity<ResponseStreamInfoDTO> streamInfo() {
        return ResponseEntity.ok(channelService.getStreamInfo());
    }

    @PostMapping("/studio/change-stream-info")
    public ResponseEntity<ResponseStreamInfoDTO> changeStreamInfo(@Valid @RequestBody RequestChangeStreamInfoDTO dto) {
        return ResponseEntity.ok(channelService.changeStreamInfo(dto));
    }

    @GetMapping("/studio/stream-key")
    public ResponseEntity<ResponseStreamKeyDTO> streamKey() {
        return ResponseEntity.ok(channelService.getStreamKey());
    }
}
