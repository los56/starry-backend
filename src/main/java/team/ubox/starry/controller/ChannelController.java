package team.ubox.starry.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import team.ubox.starry.service.dto.CustomResponse;
import team.ubox.starry.service.dto.channel.ChannelDTO;
import team.ubox.starry.service.dto.stream.ResponseFollowListDTO;
import team.ubox.starry.exception.CustomError;
import team.ubox.starry.exception.CustomException;
import team.ubox.starry.service.ChannelService;
import team.ubox.starry.helper.UUIDHelper;

@RestController
@RequestMapping("/api/channel")
@RequiredArgsConstructor
public class ChannelController {
    private final ChannelService channelService;

    @PostMapping("/open")
    public CustomResponse<ChannelDTO.Response> openChannel() {
        return new CustomResponse<>(channelService.open());
    }

    @GetMapping("")
    public CustomResponse<ChannelDTO.Response> getChannelDetail(@RequestParam String id) {
        if(id == null || id.isEmpty()) {
            throw new CustomException(CustomError.BLANK_CHANNEL_ID);
        } else if(!UUIDHelper.checkParsable(id)) {
            throw new CustomException(CustomError.INVALID_CHANNEL_ID);
        }

        return new CustomResponse<>(channelService.channelData(id));
    }

    @GetMapping("/follow-list")
    public CustomResponse<ResponseFollowListDTO> getUserFollowingList() {
        return new CustomResponse<>(channelService.followList());
    }

    @GetMapping("/follow")
    public CustomResponse<Boolean> doFollow(@RequestParam String to) {
        if(to == null || to.isEmpty()) {
            throw new CustomException(CustomError.BLANK_CHANNEL_ID);
        } else if(!UUIDHelper.checkParsable(to)) {
            throw new CustomException(CustomError.INVALID_CHANNEL_ID);
        }

        return new CustomResponse<>(channelService.follow(to));
    }

    @GetMapping("/un-follow")
    public CustomResponse<Boolean> doUnFollow(@RequestParam String to) {
        if(to == null || to.isEmpty()) {
            throw new CustomException(CustomError.BLANK_CHANNEL_ID);
        } else if(!UUIDHelper.checkParsable(to)) {
            throw new CustomException(CustomError.INVALID_CHANNEL_ID);
        }

        return new CustomResponse<>(channelService.unFollow(to));
    }

    @GetMapping("/relation")
    public CustomResponse<ChannelDTO.ResponseRelation> getRelation(@RequestParam String channelId) {
        if(!UUIDHelper.checkParsable(channelId)) {
            throw new CustomException(CustomError.INVALID_CHANNEL_ID);
        }

        return new CustomResponse<>(channelService.relation(channelId));
    }
}
