package team.ubox.starry.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import team.ubox.starry.service.dto.StarryResponse;
import team.ubox.starry.service.dto.channel.ChannelDTO;
import team.ubox.starry.service.dto.stream.ResponseFollowListDTO;
import team.ubox.starry.exception.StarryError;
import team.ubox.starry.exception.StarryException;
import team.ubox.starry.service.ChannelService;
import team.ubox.starry.helper.UUIDHelper;

@RestController
@RequestMapping("/api/channel")
@RequiredArgsConstructor
public class ChannelController {
    private final ChannelService channelService;

    @PostMapping("/open")
    public StarryResponse<ChannelDTO.Response> openChannel() {
        return new StarryResponse<>(channelService.open());
    }

    @GetMapping("")
    public StarryResponse<ChannelDTO.Response> getChannelDetail(@RequestParam String id) {
        if(id == null || id.isEmpty()) {
            throw new StarryException(StarryError.BLANK_CHANNEL_ID);
        } else if(!UUIDHelper.checkParsable(id)) {
            throw new StarryException(StarryError.INVALID_CHANNEL_ID);
        }

        return new StarryResponse<>(channelService.channelData(id));
    }

    @GetMapping("/follow-list")
    public StarryResponse<ResponseFollowListDTO> getUserFollowingList() {
        return new StarryResponse<>(channelService.followList());
    }

    @PostMapping("/follow")
    public StarryResponse<Boolean> doFollow(@RequestParam String toChannelId) {
        if(toChannelId == null || toChannelId.isEmpty()) {
            throw new StarryException(StarryError.BLANK_CHANNEL_ID);
        } else if(!UUIDHelper.checkParsable(toChannelId)) {
            throw new StarryException(StarryError.INVALID_CHANNEL_ID);
        }

        return new StarryResponse<>(channelService.follow(toChannelId));
    }

    @PostMapping("/un-follow")
    public StarryResponse<Boolean> doUnFollow(@RequestParam String toChannelId) {
        if(toChannelId == null || toChannelId.isEmpty()) {
            throw new StarryException(StarryError.BLANK_CHANNEL_ID);
        } else if(!UUIDHelper.checkParsable(toChannelId)) {
            throw new StarryException(StarryError.INVALID_CHANNEL_ID);
        }

        return new StarryResponse<>(channelService.unFollow(toChannelId));
    }

    @GetMapping("/relation")
    public StarryResponse<ChannelDTO.ResponseRelation> getRelation(@RequestParam String channelId) {
        if(!UUIDHelper.checkParsable(channelId)) {
            throw new StarryException(StarryError.INVALID_CHANNEL_ID);
        }

        return new StarryResponse<>(channelService.relation(channelId));
    }
}
