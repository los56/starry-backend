package team.ubox.starry.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import team.ubox.starry.dto.user.*;
import team.ubox.starry.entity.User;
import team.ubox.starry.repository.UserRepository;
import team.ubox.starry.security.provider.JwtProvider;
import team.ubox.starry.util.AuthUtil;
import team.ubox.starry.util.UUIDUtil;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final JwtProvider jwtProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public ResponseLoginDTO login(@Valid RequestLoginDTO dto) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        return jwtProvider.createToken(authentication);
    }

    public ResponseLoginDTO reissue(String refreshToken) {
        String userId = jwtProvider.validateRefreshToken(refreshToken);
        if(userId == null) {
            return null;
        }
        User user = userRepository.findById(UUIDUtil.stringToUUID(userId)).orElseThrow(() -> new IllegalStateException("잘못된 사용자 정보"));
        String accessToken = jwtProvider.createAccessToken(user);

        return new ResponseLoginDTO(accessToken, null);
    }

    public ResponseRegisterDTO register(@Valid RequestRegisterDTO dto) {
        Timestamp nowTimestamp = Timestamp.from(Instant.now());

        User result =  userRepository.save(User.builder()
                        .username(dto.getUsername())
                        .password(encryptPassword(dto.getPassword()))
                        .nickname(dto.getNickname())
                        .email(dto.getEmail())
                        .userRole("USER")
                        .createDate(nowTimestamp)
                        .passwordChangeDate(nowTimestamp)
                        .build());

        return ResponseRegisterDTO.from(result);
    }

    public ResponseUserDTO userInfo() {
        User authUser = AuthUtil.getAuthUser().orElseThrow(() -> new IllegalStateException("잘못된 사용자입니다."));
        User user = userRepository.findById(authUser.getId()).orElseThrow(() -> new IllegalStateException("잘못된 사용자입니다."));

        return ResponseUserDTO.from(user);
    }

    public Boolean checkUsernameDuplicate(String username) {
        Optional<User> result = userRepository.findByUsername(username);
        return result.isPresent();
    }

    public Boolean checkNicknameDuplicate(String nickname) {
        Optional<User> result = userRepository.findByNickname(nickname);
        return result.isPresent();
    }

    public Boolean checkEmailDuplicate(String email) {
        Optional<User> result = userRepository.findByEmail(email);
        return result.isPresent();
    }

    private String encryptPassword(String plainPassword) {
        return passwordEncoder.encode(plainPassword);
    }
}
