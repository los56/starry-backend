package team.ubox.starry.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.ubox.starry.service.dto.channel.ChannelDTO;
import team.ubox.starry.service.dto.channel.ResponseStreamKeyDTO;
import team.ubox.starry.service.dto.stream.RequestChangeStreamInfoDTO;
import team.ubox.starry.service.dto.channel.ResponseStreamInfoDTO;
import team.ubox.starry.service.dto.stream.ResponseFollowListDTO;
import team.ubox.starry.service.dto.stream.ResponseStreamDTO;
import team.ubox.starry.repository.entity.Channel;
import team.ubox.starry.repository.entity.Follow;
import team.ubox.starry.repository.entity.redis.StreamRedis;
import team.ubox.starry.repository.entity.User;
import team.ubox.starry.exception.CustomError;
import team.ubox.starry.exception.CustomException;
import team.ubox.starry.repository.ChannelRepository;
import team.ubox.starry.repository.FollowRepository;
import team.ubox.starry.repository.redis.StreamRedisRepository;
import team.ubox.starry.repository.UserRepository;
import team.ubox.starry.types.StreamStatus;
import team.ubox.starry.types.UserRole;
import team.ubox.starry.helper.AuthHelper;
import team.ubox.starry.helper.StringHelper;
import team.ubox.starry.helper.UUIDHelper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChannelService {
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final StreamRedisRepository streamRedisRepository;
    private final FollowRepository followRepository;

    public static final Integer STREAM_KEY_LENGTH = 32;

    @Transactional
    public ChannelDTO.Response open() {
        User authUser = AuthHelper.getAuthUser().orElseThrow(() -> new CustomException(CustomError.INVALID_TOKEN));

        Optional<Channel> findResult = channelRepository.findById(authUser.getId());
        if(findResult.isPresent()) {
            throw new CustomException(CustomError.ALREADY_OPENED_CHANNEL);
        }


        User user = userRepository.findByUsername(authUser.getUsername()).orElseThrow(() -> new CustomException(CustomError.NOT_FOUND_USER));
        user.updateRole(new UserRole[] {UserRole.USER, UserRole.STREAMER});

        String streamKey = StringHelper.generateRandomString(STREAM_KEY_LENGTH);
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
        Channel channel = channelRepository.findById(UUIDHelper.stringToUUID(channelId)).orElseThrow(() -> new CustomException(CustomError.NOT_FOUND_CHANNEL));
        ChannelDTO.Response dto = ChannelDTO.Response.from(channel);
        dto.setFollowers(followRepository.countByToUser(channel.getId()));

        return dto;
    }

    @Transactional
    public ResponseStreamKeyDTO generateStreamKey() {
        User authUser = AuthHelper.getAuthUser().orElseThrow(() -> new CustomException(CustomError.INVALID_TOKEN));

        String key = StringHelper.generateRandomString(STREAM_KEY_LENGTH);

        Channel channel = channelRepository.findById(authUser.getId()).orElseThrow(() -> new CustomException(CustomError.NOT_FOUND_CHANNEL));
        channel.updateStreamKey(key);

        ResponseStreamKeyDTO responseDto = new ResponseStreamKeyDTO();
        responseDto.setId(UUIDHelper.UUIDToString(authUser.getId()));
        responseDto.setStreamKey(key);

        return responseDto;
    }

    @Transactional
    public ResponseStreamInfoDTO changeStreamInfo(@Valid RequestChangeStreamInfoDTO dto) {
        User user = AuthHelper.getAuthUser().orElseThrow(() -> new CustomException(CustomError.INVALID_TOKEN));
        Channel channel = channelRepository.findById(user.getId()).orElseThrow(() -> new CustomException(CustomError.NOT_FOUND_CHANNEL));

        channel.updateStreamInfo(dto.getStreamTitle(), dto.getStreamCategory());

        ResponseStreamInfoDTO resDto = new ResponseStreamInfoDTO();
        resDto.setStreamTitle(channel.getStreamTitle());
        resDto.setStreamCategory(channel.getStreamCategory());

        return resDto;
    }

    public ResponseStreamInfoDTO getStreamInfo() {
        User user = AuthHelper.getAuthUser().orElseThrow(() -> new CustomException(CustomError.INVALID_TOKEN));
        Channel channel = channelRepository.findById(user.getId()).orElseThrow(() -> new CustomException(CustomError.NOT_FOUND_CHANNEL));

        ResponseStreamInfoDTO dto = new ResponseStreamInfoDTO();
        dto.setStreamTitle(channel.getStreamTitle());
        dto.setStreamCategory(channel.getStreamCategory());

        return dto;
    }

    public ResponseStreamKeyDTO getStreamKey() {
        User user = AuthHelper.getAuthUser().orElseThrow(() -> new CustomException(CustomError.INVALID_TOKEN));
        Channel channel = channelRepository.findById(user.getId()).orElseThrow(() -> new CustomException(CustomError.NOT_FOUND_CHANNEL));

        ResponseStreamKeyDTO dto = new ResponseStreamKeyDTO();
        dto.setId(UUIDHelper.UUIDToString(channel.getId()));
        dto.setStreamKey(channel.getStreamKey());

        return dto;
    }


    public Boolean follow(String id) {
        User user = AuthHelper.getAuthUser().orElseThrow(() -> new CustomException(CustomError.INVALID_TOKEN));
        Optional<Follow> follow = followRepository.findByFromUserAndToUser(user.getId(), UUIDHelper.stringToUUID(id));
        if(follow.isPresent()) {
            throw new CustomException(CustomError.ALREADY_FOLLOWED_CHANNEL);
        }

        Optional<Channel> channel = channelRepository.findById(UUIDHelper.stringToUUID(id));
        if(channel.isEmpty()) {
            throw new CustomException(CustomError.NOT_FOUND_CHANNEL);
        }
        followRepository.save(new Follow(user.getId(), channel.get().getId()));

        return true;
    }

    public Boolean unFollow(String id) {
        User user = AuthHelper.getAuthUser().orElseThrow(() -> new CustomException(CustomError.INVALID_TOKEN));
        Optional<Follow> follow = followRepository.findByFromUserAndToUser(user.getId(), UUIDHelper.stringToUUID(id));
        if(follow.isEmpty()) {
            throw new CustomException(CustomError.NOT_FOLLOWED_CHANNEL);
        }
        followRepository.delete(follow.get());

        return true;
    }

    public ResponseFollowListDTO followList() {
        User user = AuthHelper.getAuthUser().orElseThrow(() -> new CustomException(CustomError.INVALID_TOKEN));
        List<UUID> follows = followRepository.findAllByFromUser(user.getId()).stream().map(Follow::getToUser).toList();
        List<Channel> channels = channelRepository.findAllById(follows);
        List<ResponseStreamDTO> streams = channels.stream().map(this::mappingStreamDTO).toList();

        return new ResponseFollowListDTO(streams);
    }

    public ChannelDTO.ResponseRelation relation(String channelId) {
        User user = AuthHelper.getAuthUser().orElseThrow(() -> new CustomException(CustomError.INVALID_TOKEN));
        Optional<Follow> follow = followRepository.findByFromUserAndToUser(user.getId(), UUIDHelper.stringToUUID(channelId));

        return new ChannelDTO.ResponseRelation(channelId, follow.isPresent());
    }


    private ResponseStreamDTO mappingStreamDTO(Channel channel) {
        ResponseStreamDTO responseStreamDTO = ResponseStreamDTO.builder()
                .channel(ChannelDTO.Response.from(channel))
                .streamTitle(channel.getStreamTitle())
                .streamCategory(channel.getStreamCategory())
                .status(StreamStatus.CLOSE)
                .build();

        Optional<StreamRedis> optionalStream = streamRedisRepository.findById(channel.getId());
        if(optionalStream.isPresent()) {
            StreamRedis streamRedis = optionalStream.get();
            responseStreamDTO.setStreamId(streamRedis.getStreamId());
            responseStreamDTO.setStatus(StreamStatus.LIVE);
            responseStreamDTO.setViewersCount(streamRedis.getViewers());
        }

        return responseStreamDTO;
    }
}
