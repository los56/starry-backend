package team.ubox.starry.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.ubox.starry.dto.channel.ResponseChannelDTO;
import team.ubox.starry.dto.stream.ResponseStreamDTO;
import team.ubox.starry.entity.Channel;
import team.ubox.starry.entity.Stream;
import team.ubox.starry.repository.ChannelRepository;
import team.ubox.starry.repository.StreamRedisRepository;
import team.ubox.starry.types.StreamStatus;
import team.ubox.starry.util.CommonUtil;
import team.ubox.starry.util.UUIDUtil;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

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
        Stream stream = streamRedisRepository.findById(channel.getId()).orElseThrow(() -> new IllegalStateException("존재하지 않는 방송"));
        System.out.println("stream.getO[p] = " + stream.getOpenTime());
        channel.updateLastStream(stream.getStreamId(), Timestamp.valueOf(stream.getOpenTime()), Timestamp.from(Instant.now()));

        streamRedisRepository.deleteById(channel.getId());
        return true;
    }

    public ResponseStreamDTO stream(String channelId) {
        Optional<Stream> optionalStream = streamRedisRepository.findById(UUIDUtil.stringToUUID(channelId));
        Channel channel = channelRepository.findById(UUIDUtil.stringToUUID(channelId)).orElseThrow(() -> new IllegalStateException("잘못된 채널입니다."));

        ResponseStreamDTO responseStreamDTO = ResponseStreamDTO.builder()
                .channel(ResponseChannelDTO.from(channel))
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
}
