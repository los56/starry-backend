package team.ubox.starry.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.ubox.starry.service.dto.StarryResponse;
import team.ubox.starry.service.UserService;
import team.ubox.starry.service.dto.user.LoginDTO;
import team.ubox.starry.service.dto.user.RegisterDTO;
import team.ubox.starry.service.dto.user.UserDTO;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public StarryResponse<LoginDTO.Response> login(@Valid @RequestBody LoginDTO.Request dto) {
        return new StarryResponse<>(userService.login(dto));
    }

    @PatchMapping("/reissue")
    public StarryResponse<LoginDTO.Response> reissue(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader("refreshToken");
        return new StarryResponse<>(userService.reissue(token));
    }

    @PostMapping("/register")
    public StarryResponse<RegisterDTO.Response> register(@Valid @RequestBody RegisterDTO.Request dto) {
        return new StarryResponse<>(userService.register(dto));
    }

    @GetMapping("/user-info")
    public StarryResponse<UserDTO.Response> getUserInfo() {
        return new StarryResponse<>(userService.userInfo());
    }

    @GetMapping("/duplicate")
    public StarryResponse<Boolean> checkDuplicate(@RequestParam String method, @RequestParam String data) {
        Boolean result;
        if(data.isEmpty()) {
            throw new IllegalArgumentException("data에 값이 없습니다.");
        }

        switch(method) {
            case "username" -> result = userService.checkUsernameDuplicate(data);
            case "nickname" -> result = userService.checkNicknameDuplicate(data);
            case "email" -> result = userService.checkEmailDuplicate(data);
            default -> throw new IllegalArgumentException("method가 잘못되었습니다.");
        }

        return new StarryResponse<>(result);
    }

}
