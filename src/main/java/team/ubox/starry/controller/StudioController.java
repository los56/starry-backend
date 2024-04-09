package team.ubox.starry.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import team.ubox.starry.service.dto.StarryResponse;
import team.ubox.starry.service.dto.channel.ResponseStreamInfoDTO;
import team.ubox.starry.service.dto.channel.ResponseStreamKeyDTO;
import team.ubox.starry.service.dto.stream.RequestChangeStreamInfoDTO;
import team.ubox.starry.service.ChannelService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/studio")
public class StudioController {
    private final ChannelService channelService;

    @GetMapping("/studio/regen-key")
    public StarryResponse<ResponseStreamKeyDTO> generateStreamKey() {
        return new StarryResponse<>(channelService.generateStreamKey());
    }

    @GetMapping("/studio/stream-info")
    public StarryResponse<ResponseStreamInfoDTO> getStreamInfo() {
        return new StarryResponse<>(channelService.getStreamInfo());
    }

    @PostMapping("/studio/change-stream-info")
    public StarryResponse<ResponseStreamInfoDTO> changeStreamInfo(@Valid @RequestBody RequestChangeStreamInfoDTO dto) {
        return new StarryResponse<>(channelService.changeStreamInfo(dto));
    }

    @GetMapping("/studio/stream-key")
    public StarryResponse<ResponseStreamKeyDTO> getStreamKey() {
        return new StarryResponse<>(channelService.getStreamKey());
    }
}
