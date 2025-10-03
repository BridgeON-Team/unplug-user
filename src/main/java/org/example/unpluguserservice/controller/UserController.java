package org.example.unpluguserservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.unpluguserservice.common.ApiResponse;
import org.example.unpluguserservice.dto.UserRequestDto;
import org.example.unpluguserservice.dto.UserResponseDto;
import org.example.unpluguserservice.entity.User;
import org.example.unpluguserservice.repository.UserRepository;
import org.example.unpluguserservice.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/signup")
    public ApiResponse<UserResponseDto> registerUser(@Valid @RequestBody UserRequestDto request){
        UserResponseDto response = userService.createUser(request, passwordEncoder);
        return new ApiResponse<>(true, "회원가입이 정상적으로 완료되었습니다.", response);
    }

    @GetMapping("/check/username")
    public ApiResponse<String> checkUsername(@RequestParam("username") String username){
        userService.checkUsableUsername(username);
        return new ApiResponse<>(true, "사용 가능한 아이디입니다.", username);
    }

    @GetMapping("/check/nickname")
    public ApiResponse<String> checkNickname(@RequestParam("nickname") String nickname){
        userService.checkUsableNickname(nickname);
        return new ApiResponse<>(true, "사용 가능한 닉네임입니다.", nickname);
    }

    // 테스트용 API
    @GetMapping("/me")
    public UserInfo me(@RequestHeader(value = "X-Auth-Username", required = false) String username) {
        if (username == null) throw new IllegalArgumentException("username required");
        User user = userRepository.findByUsername(username).orElseThrow();
        return new UserInfo(user.getUserId(), user.getUsername());
    }
    public record UserInfo(Long id, String username) {}
}
