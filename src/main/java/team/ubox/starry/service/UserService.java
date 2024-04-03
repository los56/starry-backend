package team.ubox.starry.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import team.ubox.starry.dto.user.*;
import team.ubox.starry.entity.User;
import team.ubox.starry.exception.StarryError;
import team.ubox.starry.exception.StarryException;
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

    public LoginDTO.Response login(@Valid LoginDTO.Request dto) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        return jwtProvider.createToken(authentication);
    }

    public LoginDTO.Response reissue(String refreshToken) {
        String userId = jwtProvider.validateRefreshToken(refreshToken);
        if(userId == null) {
            throw new StarryException(StarryError.INVALID_TOKEN);
        }
        User user = userRepository.findById(UUIDUtil.stringToUUID(userId)).orElseThrow(() -> new StarryException(StarryError.INVALID_TOKEN));
        String accessToken = jwtProvider.createAccessToken(user);

        return new LoginDTO.Response(accessToken, null);
    }

    public RegisterDTO.Response register(@Valid RegisterDTO.Request dto) {
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

        return RegisterDTO.Response.from(result);
    }

    public UserDTO.Response userInfo() {
        User authUser = AuthUtil.getAuthUser().orElseThrow(() -> new StarryException(StarryError.INVALID_TOKEN));
        User user = userRepository.findById(authUser.getId()).orElseThrow(() -> new StarryException(StarryError.INVALID_TOKEN));

        return UserDTO.Response.from(user);
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
