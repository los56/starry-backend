package team.ubox.starry.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import team.ubox.starry.repository.entity.CustomUserDetail;
import team.ubox.starry.repository.entity.User;
import team.ubox.starry.exception.CustomError;
import team.ubox.starry.exception.CustomException;
import team.ubox.starry.repository.UserRepository;
import team.ubox.starry.security.provider.JwtProvider;
import team.ubox.starry.service.dto.user.LoginDTO;
import team.ubox.starry.service.dto.user.RegisterDTO;
import team.ubox.starry.service.dto.user.UserDTO;
import team.ubox.starry.helper.AuthHelper;
import team.ubox.starry.helper.UUIDHelper;

import java.sql.Time;
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
            throw new CustomException(CustomError.INVALID_TOKEN);
        }
        User user = userRepository.findById(UUIDHelper.stringToUUID(userId)).orElseThrow(() -> new CustomException(CustomError.INVALID_TOKEN));
        CustomUserDetail userDetail = CustomUserDetail.from(user);

        String accessToken = jwtProvider.createAccessToken(userDetail);

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
        CustomUserDetail userDetail = AuthHelper.getAuthUser().orElseThrow(() -> new CustomException(CustomError.INVALID_TOKEN));
        User user = userRepository.findById(userDetail.getId()).orElseThrow(() -> new CustomException(CustomError.NOT_FOUND_USER));

        return UserDTO.Response.from(user);
    }

    @Transactional
    public UserDTO.Response changeUserInfo(UserDTO.RequestChangeInfo requestChangeInfo) {
        CustomUserDetail userDetail = AuthHelper.getAuthUser().orElseThrow(() -> new CustomException(CustomError.INVALID_TOKEN));
        User user = userRepository.findById(userDetail.getId()).orElseThrow(() -> new CustomException(CustomError.NOT_FOUND_USER));

        user.updateNickname(requestChangeInfo.getNickname());
        user.updateProfileImageUrl(requestChangeInfo.getProfileImageUrl());

        return UserDTO.Response.from(user);
    }

    @Transactional
    public UserDTO.Response changePassword(UserDTO.RequestChangePassword requestChangePassword) {
        CustomUserDetail userDetail = AuthHelper.getAuthUser().orElseThrow(() -> new CustomException(CustomError.INVALID_TOKEN));
        User user = userRepository.findById(userDetail.getId()).orElseThrow(() -> new CustomException(CustomError.NOT_FOUND_USER));

        boolean isValidPassword = BCrypt.checkpw(requestChangePassword.getCurrentPassword(), user.getPassword());
        if(!isValidPassword) {
            throw new CustomException(CustomError.NOT_MATCH_PASSWORD);
        }

        Timestamp nowTimestamp = Timestamp.from(Instant.now());
        user.updatePassword(requestChangePassword.getNewPassword(), nowTimestamp);
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
