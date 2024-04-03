package team.ubox.starry.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.ubox.starry.dto.channel.ChannelDTO;
import team.ubox.starry.dto.stream.ResponseFollowListDTO;
import team.ubox.starry.dto.stream.ResponseStreamDTO;
import team.ubox.starry.entity.Channel;
import team.ubox.starry.entity.Follow;
import team.ubox.starry.entity.Stream;
import team.ubox.starry.entity.User;
import team.ubox.starry.exception.StarryError;
import team.ubox.starry.exception.StarryException;
import team.ubox.starry.repository.ChannelRepository;
import team.ubox.starry.repository.FollowRepository;
import team.ubox.starry.repository.StreamRedisRepository;
import team.ubox.starry.types.StreamStatus;
import team.ubox.starry.util.AuthUtil;
import team.ubox.starry.util.CommonUtil;
import team.ubox.starry.util.UUIDUtil;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class StreamService {
    private final ChannelRepository channelRepository;
    private final FollowRepository followRepository;
    private final StreamRedisRepository streamRedisRepository;

    public static final Integer STREAM_KEY_LENGTH = 24;

    public Boolean existChannel(String streamKey) {
        Optional<Channel> channel = channelRepository.findByStreamKey(streamKey);

        return channel.isPresent();
    }

    public String requestPublish(String streamKey) {
        Optional<Channel> optionalChannel = channelRepository.findByStreamKey(streamKey);
        if(optionalChannel.isEmpty()){
            return null;
        }

        Channel channel = optionalChannel.get();
        String streamId = CommonUtil.generateRandomString(STREAM_KEY_LENGTH);
        streamRedisRepository.save(Stream.builder()
                .id(channel.getId())
                .streamId(streamId)
                .openTime(Timestamp.from(Instant.now()).toString())
                .build());

        return streamId;
    }

    @Transactional
    public Boolean endPublish(String streamKey) {
        Optional<Channel> optionalChannel = channelRepository.findByStreamKey(streamKey);
        if(optionalChannel.isEmpty()) {
            return false;
        }
        Channel channel = optionalChannel.get();
        Stream stream = streamRedisRepository.findById(channel.getId()).orElseThrow(() -> new StarryException(StarryError.NOT_FOUND_STREAM));
        System.out.println("stream.getO[p] = " + stream.getOpenTime());
        channel.updateLastStream(stream.getStreamId(), Timestamp.valueOf(stream.getOpenTime()), Timestamp.from(Instant.now()));

        streamRedisRepository.deleteById(channel.getId());
        return true;
    }

    public ResponseStreamDTO stream(String channelId) {
        Optional<Stream> optionalStream = streamRedisRepository.findById(UUIDUtil.stringToUUID(channelId));
        Channel channel = channelRepository.findById(UUIDUtil.stringToUUID(channelId)).orElseThrow(() -> new StarryException(StarryError.NOT_FOUND_CHANNEL));

        ResponseStreamDTO responseStreamDTO = ResponseStreamDTO.builder()
                .channel(ChannelDTO.Response.from(channel))
                .streamTitle(channel.getStreamTitle())
                .streamCategory(channel.getStreamCategory())
                .build();

        if(optionalStream.isPresent()) {
            Stream stream = optionalStream.get();
            responseStreamDTO.setStreamId(stream.getStreamId());
            responseStreamDTO.setStatus(StreamStatus.LIVE);
        } else {
            responseStreamDTO.setStatus(StreamStatus.CLOSE);
        }

        return responseStreamDTO;
    }

    public ResponseFollowListDTO followList() {
        User user = AuthUtil.getAuthUser().orElseThrow(() -> new StarryException(StarryError.INVALID_TOKEN));
        List<UUID> follows = followRepository.findAllByFromUser(user.getId()).stream().map(Follow::getToUser).toList();
        List<Channel> channels = channelRepository.findAllById(follows);
        List<ResponseStreamDTO> streams = channels.stream().map(this::mappingStreamDTO).toList();

        return new ResponseFollowListDTO(streams);
    }

    private ResponseStreamDTO mappingStreamDTO(Channel channel) {
        ResponseStreamDTO responseStreamDTO = ResponseStreamDTO.builder()
                .channel(ChannelDTO.Response.from(channel))
                .streamTitle(channel.getStreamTitle())
                .streamCategory(channel.getStreamCategory())
                .status(StreamStatus.CLOSE)
                .build();

        Optional<Stream> optionalStream = streamRedisRepository.findById(channel.getId());
        if(optionalStream.isPresent()) {
            Stream stream = optionalStream.get();
            responseStreamDTO.setStreamId(stream.getStreamId());
            responseStreamDTO.setStatus(StreamStatus.LIVE);
        }

        return responseStreamDTO;
    }
}
