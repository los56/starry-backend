package team.ubox.starry.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.ubox.starry.service.dto.channel.ChannelDTO;
import team.ubox.starry.service.dto.stream.ResponseStreamDTO;
import team.ubox.starry.repository.entity.Channel;
import team.ubox.starry.repository.entity.redis.StreamRedis;
import team.ubox.starry.exception.StarryError;
import team.ubox.starry.exception.StarryException;
import team.ubox.starry.repository.ChannelRepository;
import team.ubox.starry.repository.FollowRepository;
import team.ubox.starry.repository.redis.StreamRedisRepository;
import team.ubox.starry.types.StreamStatus;
import team.ubox.starry.helper.StringHelper;
import team.ubox.starry.helper.UUIDHelper;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class StreamService {
    private final ChannelRepository channelRepository;
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
        String streamId = StringHelper.generateRandomString(STREAM_KEY_LENGTH);
        streamRedisRepository.save(StreamRedis.builder()
                .id(channel.getId())
                .streamId(streamId)
                .openTime(Timestamp.from(Instant.now()).toString())
                .build());

        return streamId;
    }

    @Transactional
    public void endPublish(String streamKey) {
        Optional<Channel> optionalChannel = channelRepository.findByStreamKey(streamKey);
        if(optionalChannel.isEmpty()) {
            throw new StarryException(StarryError.NOT_FOUND_CHANNEL);
        }

        Channel channel = optionalChannel.get();

        StreamRedis streamRedis = streamRedisRepository.findById(channel.getId()).orElseThrow(() -> new StarryException(StarryError.NOT_FOUND_STREAM));
        channel.updateLastStream(streamRedis.getStreamId(), Timestamp.valueOf(streamRedis.getOpenTime()), Timestamp.from(Instant.now()));

        streamRedisRepository.deleteById(channel.getId());
    }

    public ResponseStreamDTO stream(String channelId) {
        Optional<StreamRedis> optionalStream = streamRedisRepository.findById(UUIDHelper.stringToUUID(channelId));
        Channel channel = channelRepository.findById(UUIDHelper.stringToUUID(channelId)).orElseThrow(() -> new StarryException(StarryError.NOT_FOUND_CHANNEL));

        ResponseStreamDTO responseStreamDTO = ResponseStreamDTO.builder()
                .channel(ChannelDTO.Response.from(channel))
                .streamTitle(channel.getStreamTitle())
                .streamCategory(channel.getStreamCategory())
                .build();

        if(optionalStream.isPresent()) {
            StreamRedis streamRedis = optionalStream.get();

            responseStreamDTO.setStreamId(streamRedis.getStreamId());
            responseStreamDTO.setStatus(StreamStatus.LIVE);
        } else {
            responseStreamDTO.setStatus(StreamStatus.CLOSE);
        }

        return responseStreamDTO;
    }
}
