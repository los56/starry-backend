package team.ubox.starry.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import team.ubox.starry.dto.channel.ChannelDTO;
import team.ubox.starry.dto.channel.ResponseStreamKeyDTO;
import team.ubox.starry.dto.stream.RequestChangeStreamInfoDTO;
import team.ubox.starry.dto.channel.ResponseStreamInfoDTO;
import team.ubox.starry.entity.Channel;
import team.ubox.starry.entity.Follow;
import team.ubox.starry.entity.User;
import team.ubox.starry.exception.StarryError;
import team.ubox.starry.exception.StarryException;
import team.ubox.starry.repository.ChannelRepository;
import team.ubox.starry.repository.FollowRepository;
import team.ubox.starry.repository.UserRepository;
import team.ubox.starry.types.UserRole;
import team.ubox.starry.util.AuthUtil;
import team.ubox.starry.util.CommonUtil;
import team.ubox.starry.util.UUIDUtil;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChannelService {
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final FollowRepository followRepository;

    public static final Integer STREAM_KEY_LENGTH = 32;

    @Transactional
    public ChannelDTO.Response open() {
        User authUser = AuthUtil.getAuthUser().orElseThrow(() -> new StarryException(StarryError.INVALID_TOKEN));

        Optional<Channel> findResult = channelRepository.findById(authUser.getId());
        if(findResult.isPresent()) {
            throw new StarryException(StarryError.ALREADY_OPENED_CHANNEL);
        }


        User user = userRepository.findByUsername(authUser.getUsername()).orElseThrow(() -> new StarryException(StarryError.NOT_FOUND_USER));
        user.updateRole(new UserRole[] {UserRole.USER, UserRole.STREAMER});

        String streamKey = CommonUtil.generateRandomString(STREAM_KEY_LENGTH);
        Channel channel = channelRepository.save(
                Channel.builder()
                        .owner(user)
                        .description("")
                        .verified(false)
                        .streamKey(streamKey)
                        .streamTitle("%s님의 방송입니다.".formatted(user.getNickname()))
                        .streamCategory("Talking")
                        .build());

        return ChannelDTO.Response.from(channel);
    }

    public ChannelDTO.Response channelData(String channelId) {
        Channel channel = channelRepository.findById(UUIDUtil.stringToUUID(channelId)).orElseThrow(() -> new StarryException(StarryError.NOT_FOUND_CHANNEL));
        ChannelDTO.Response dto = ChannelDTO.Response.from(channel);
        dto.setFollowers(followRepository.countByToUser(channel.getId()));

        return dto;
    }

    @Transactional
    public ResponseStreamKeyDTO generateStreamKey() {
        User authUser = AuthUtil.getAuthUser().orElseThrow(() -> new StarryException(StarryError.INVALID_TOKEN));

        String key = CommonUtil.generateRandomString(STREAM_KEY_LENGTH);

        Channel channel = channelRepository.findById(authUser.getId()).orElseThrow(() -> new StarryException(StarryError.NOT_FOUND_CHANNEL));
        channel.updateStreamKey(key);

        ResponseStreamKeyDTO responseDto = new ResponseStreamKeyDTO();
        responseDto.setId(UUIDUtil.UUIDToString(authUser.getId()));
        responseDto.setStreamKey(key);

        return responseDto;
    }

    @Transactional
    public ResponseStreamInfoDTO changeStreamInfo(@Valid RequestChangeStreamInfoDTO dto) {
        User user = AuthUtil.getAuthUser().orElseThrow(() -> new StarryException(StarryError.INVALID_TOKEN));
        Channel channel = channelRepository.findById(user.getId()).orElseThrow(() -> new StarryException(StarryError.NOT_FOUND_CHANNEL));

        channel.updateStreamInfo(dto.getStreamTitle(), dto.getStreamCategory());

        ResponseStreamInfoDTO resDto = new ResponseStreamInfoDTO();
        resDto.setStreamTitle(channel.getStreamTitle());
        resDto.setStreamCategory(channel.getStreamCategory());

        return resDto;
    }

    public ResponseStreamInfoDTO getStreamInfo() {
        User user = AuthUtil.getAuthUser().orElseThrow(() -> new StarryException(StarryError.INVALID_TOKEN));
        Channel channel = channelRepository.findById(user.getId()).orElseThrow(() -> new StarryException(StarryError.NOT_FOUND_CHANNEL));

        ResponseStreamInfoDTO dto = new ResponseStreamInfoDTO();
        dto.setStreamTitle(channel.getStreamTitle());
        dto.setStreamCategory(channel.getStreamCategory());

        return dto;
    }

    public ResponseStreamKeyDTO getStreamKey() {
        User user = AuthUtil.getAuthUser().orElseThrow(() -> new StarryException(StarryError.INVALID_TOKEN));
        Channel channel = channelRepository.findById(user.getId()).orElseThrow(() -> new StarryException(StarryError.NOT_FOUND_CHANNEL));

        ResponseStreamKeyDTO dto = new ResponseStreamKeyDTO();
        dto.setId(UUIDUtil.UUIDToString(channel.getId()));
        dto.setStreamKey(channel.getStreamKey());

        return dto;
    }


    public Boolean follow(String id) {
        User user = AuthUtil.getAuthUser().orElseThrow(() -> new StarryException(StarryError.INVALID_TOKEN));
        Optional<Follow> follow = followRepository.findByFromUserAndToUser(user.getId(), UUIDUtil.stringToUUID(id));
        if(follow.isPresent()) {
            throw new StarryException(StarryError.ALREADY_FOLLOWED_CHANNEL);
        }

        Optional<Channel> channel = channelRepository.findById(UUIDUtil.stringToUUID(id));
        if(channel.isEmpty()) {
            throw new StarryException(StarryError.NOT_FOUND_CHANNEL);
        }
        followRepository.save(new Follow(user.getId(), channel.get().getId()));

        return true;
    }

    public Boolean unFollow(String id) {
        User user = AuthUtil.getAuthUser().orElseThrow(() -> new StarryException(StarryError.INVALID_TOKEN));
        Optional<Follow> follow = followRepository.findByFromUserAndToUser(user.getId(), UUIDUtil.stringToUUID(id));
        if(follow.isEmpty()) {
            throw new StarryException(StarryError.NOT_FOLLOWED_CHANNEL);
        }
        followRepository.deleteById(follow.get().getIndex());

        return true;
    }
}
