package org.example.unpluguserservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.unpluguserservice.common.ApiResponse;
import org.example.unpluguserservice.dto.UserResponseDto;
import org.example.unpluguserservice.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

//    private final UserRepository userRepository;
//    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/info")
    public ApiResponse<UserResponseDto> userInfo(@RequestHeader(value = "X-Auth-Username") String username){
        UserResponseDto response = userService.getUser(username);
        return new ApiResponse<>(true, "정상적으로 조회했습니다.", response);
    }

    @DeleteMapping("/withdraw")
    public ApiResponse<?> deleteUser(@RequestHeader(value = "X-Auth-Username") String username){
        userService.deleteUser(username);
        return new ApiResponse<>(true, "정상적으로 탈퇴되었습니다.");
    }


    // 테스트용 API
//    @GetMapping("/me")
//    public UserInfo me(@RequestHeader(value = "X-Auth-Username", required = false) String username) {
//        if (username == null) throw new IllegalArgumentException("username required");
//        User user = userRepository.findByUsername(username).orElseThrow();
//        return new UserInfo(user.getUserId(), user.getUsername());
//    }
//    public record UserInfo(Long id, String username) {}
}
