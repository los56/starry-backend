package team.ubox.starry.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import team.ubox.starry.dto.StarryResponse;
import team.ubox.starry.dto.channel.ChannelDTO;
import team.ubox.starry.dto.channel.ResponseStreamKeyDTO;
import team.ubox.starry.dto.stream.RequestChangeStreamInfoDTO;
import team.ubox.starry.dto.channel.ResponseStreamInfoDTO;
import team.ubox.starry.exception.StarryError;
import team.ubox.starry.exception.StarryException;
import team.ubox.starry.service.ChannelService;
import team.ubox.starry.util.UUIDUtil;

@RestController
@RequestMapping("/api/channel")
@RequiredArgsConstructor
public class ChannelController {
    private final ChannelService channelService;

    @PostMapping("/open")
    public StarryResponse<ChannelDTO.Response> open() {
        return new StarryResponse<>(channelService.open());
    }

    @GetMapping("")
    public StarryResponse<ChannelDTO.Response> channelData(@RequestParam String id) {
        if(id == null || id.isEmpty()) {
            throw new StarryException(StarryError.BLANK_CHANNEL_ID);
        } else if(!UUIDUtil.checkParsable(id)) {
            throw new StarryException(StarryError.INVALID_CHANNEL_ID);
        }

        return new StarryResponse<>(channelService.channelData(id));
    }

    @GetMapping("/follow")
    public StarryResponse<Boolean> follow(@RequestParam String to) {
        if(to == null || to.isEmpty()) {
            throw new StarryException(StarryError.BLANK_CHANNEL_ID);
        } else if(!UUIDUtil.checkParsable(to)) {
            throw new StarryException(StarryError.INVALID_CHANNEL_ID);
        }

        return new StarryResponse<>(channelService.follow(to));
    }

    @GetMapping("/un-follow")
    public StarryResponse<Boolean> unFollow(@RequestParam String to) {
        if(to == null || to.isEmpty()) {
            throw new StarryException(StarryError.BLANK_CHANNEL_ID);
        } else if(!UUIDUtil.checkParsable(to)) {
            throw new StarryException(StarryError.INVALID_CHANNEL_ID);
        }

        return new StarryResponse<>(channelService.unFollow(to));
    }

    @GetMapping("/studio/regen-key")
    public StarryResponse<ResponseStreamKeyDTO> generateStreamKey() {
        return new StarryResponse<>(channelService.generateStreamKey());
    }

    @GetMapping("/studio/stream-info")
    public StarryResponse<ResponseStreamInfoDTO> streamInfo() {
        return new StarryResponse<>(channelService.getStreamInfo());
    }

    @PostMapping("/studio/change-stream-info")
    public StarryResponse<ResponseStreamInfoDTO> changeStreamInfo(@Valid @RequestBody RequestChangeStreamInfoDTO dto) {
        return new StarryResponse<>(channelService.changeStreamInfo(dto));
    }

    @GetMapping("/studio/stream-key")
    public StarryResponse<ResponseStreamKeyDTO> streamKey() {
        return new StarryResponse<>(channelService.getStreamKey());
    }
}
