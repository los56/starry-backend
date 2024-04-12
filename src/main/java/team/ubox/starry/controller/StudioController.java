package team.ubox.starry.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import team.ubox.starry.service.dto.CustomResponse;
import team.ubox.starry.service.dto.channel.ResponseStreamInfoDTO;
import team.ubox.starry.service.dto.channel.ResponseStreamKeyDTO;
import team.ubox.starry.service.dto.stream.RequestChangeStreamInfoDTO;
import team.ubox.starry.service.ChannelService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/studio")
public class StudioController {
    private final ChannelService channelService;

    @GetMapping("/regen-key")
    public CustomResponse<ResponseStreamKeyDTO> generateStreamKey() {
        return new CustomResponse<>(channelService.generateStreamKey());
    }

    @GetMapping("/stream-info")
    public CustomResponse<ResponseStreamInfoDTO> getStreamInfo() {
        return new CustomResponse<>(channelService.getStreamInfo());
    }

    @PostMapping("/change-stream-info")
    public CustomResponse<ResponseStreamInfoDTO> changeStreamInfo(@Valid @RequestBody RequestChangeStreamInfoDTO dto) {
        return new CustomResponse<>(channelService.changeStreamInfo(dto));
    }

    @GetMapping("/stream-key")
    public CustomResponse<ResponseStreamKeyDTO> getStreamKey() {
        return new CustomResponse<>(channelService.getStreamKey());
    }
}
