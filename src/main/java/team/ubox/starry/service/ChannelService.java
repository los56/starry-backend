package team.ubox.starry.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import team.ubox.starry.dto.channel.ResponseChannelDTO;
import team.ubox.starry.dto.channel.ResponseStreamKeyDTO;
import team.ubox.starry.dto.stream.RequestChangeStreamInfoDTO;
import team.ubox.starry.dto.channel.ResponseStreamInfoDTO;
import team.ubox.starry.entity.Channel;
import team.ubox.starry.entity.User;
import team.ubox.starry.repository.ChannelRepository;
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

    public static final Integer STREAM_KEY_LENGTH = 32;

    @Transactional
    public ResponseChannelDTO open() {
        User authUser = AuthUtil.getAuthUser().orElseThrow(() -> new IllegalStateException("잘못된 사용자입니다."));

        Optional<Channel> findResult = channelRepository.findById(authUser.getId());
        if(findResult.isPresent()) {
            throw new IllegalStateException("이미 채널이 개설된 사용자입니다.");
        }


        User user = userRepository.findByUsername(authUser.getUsername()).orElseThrow(() -> new UsernameNotFoundException("잘못된 사용자 정보"));
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

        return ResponseChannelDTO.from(channel);
    }

    public ResponseChannelDTO channelData(String channelId) {
        Channel channel = channelRepository.findById(UUIDUtil.stringToUUID(channelId)).orElseThrow(() -> new IllegalStateException("존재하지 않는 채널"));

        return ResponseChannelDTO.from(channel);
    }

    @Transactional
    public ResponseStreamKeyDTO generateStreamKey() {
        User authUser = AuthUtil.getAuthUser().orElseThrow(() -> new IllegalStateException("잘못된 사용자입니다."));

        String key = CommonUtil.generateRandomString(STREAM_KEY_LENGTH);

        Channel channel = channelRepository.findById(authUser.getId()).orElseThrow(() -> new IllegalStateException("잘못된 사용자입니다."));
        channel.updateStreamKey(key);

        ResponseStreamKeyDTO responseDto = new ResponseStreamKeyDTO();
        responseDto.setId(UUIDUtil.UUIDToString(authUser.getId()));
        responseDto.setStreamKey(key);

        return responseDto;
    }

    @Transactional
    public ResponseStreamInfoDTO changeStreamInfo(@Valid RequestChangeStreamInfoDTO dto) {
        User user = AuthUtil.getAuthUser().orElseThrow(() -> new IllegalStateException("잘못된 사용자입니다."));
        Channel channel = channelRepository.findById(user.getId()).orElseThrow(() -> new IllegalStateException("채널이 없습니다."));

        channel.updateStreamInfo(dto.getStreamTitle(), dto.getStreamCategory());

        ResponseStreamInfoDTO resDto = new ResponseStreamInfoDTO();
        resDto.setStreamTitle(channel.getStreamTitle());
        resDto.setStreamCategory(channel.getStreamCategory());

        return resDto;
    }

    public ResponseStreamInfoDTO getStreamInfo() {
        User user = AuthUtil.getAuthUser().orElseThrow(() -> new IllegalStateException("잘못된 사용자입니다."));
        Channel channel = channelRepository.findById(user.getId()).orElseThrow(() -> new IllegalStateException("채널이 없습니다."));

        ResponseStreamInfoDTO dto = new ResponseStreamInfoDTO();
        dto.setStreamTitle(channel.getStreamTitle());
        dto.setStreamCategory(channel.getStreamCategory());

        return dto;
    }

    public ResponseStreamKeyDTO getStreamKey() {
        User user = AuthUtil.getAuthUser().orElseThrow(() -> new IllegalStateException("잘못된 사용자입니다."));
        Channel channel = channelRepository.findById(user.getId()).orElseThrow(() -> new IllegalStateException("채널이 없습니다."));

        ResponseStreamKeyDTO dto = new ResponseStreamKeyDTO();
        dto.setId(UUIDUtil.UUIDToString(channel.getId()));
        dto.setStreamKey(channel.getStreamKey());

        return dto;
    }
}
