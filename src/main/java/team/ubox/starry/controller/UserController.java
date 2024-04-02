package team.ubox.starry.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.ubox.starry.dto.user.*;
import team.ubox.starry.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseLoginDTO> login(@Valid @RequestBody RequestLoginDTO dto) {
        return ResponseEntity.ok(userService.login(dto));
    }

    @PatchMapping("/reissue")
    public ResponseEntity<ResponseLoginDTO> reissue(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader("refreshToken");
        return ResponseEntity.ok(userService.reissue(token));
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseRegisterDTO> register(@Valid @RequestBody RequestRegisterDTO dto) {
        return ResponseEntity.ok(userService.register(dto));
    }

    @GetMapping("/user-info")
    public ResponseEntity<ResponseUserDTO> userInfo() {
        return ResponseEntity.ok(userService.userInfo());
    }

    @GetMapping("/duplicate")
    public ResponseEntity<Boolean> duplicateCheck(@RequestParam String method, @RequestParam String data) {
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

        return ResponseEntity.ok(result);
    }
}
