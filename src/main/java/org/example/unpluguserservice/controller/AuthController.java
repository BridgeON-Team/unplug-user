package org.example.unpluguserservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.unpluguserservice.common.ApiResponse;
import org.example.unpluguserservice.dto.UserRequestDto;
import org.example.unpluguserservice.dto.UserResponseDto;
import org.example.unpluguserservice.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public ApiResponse<UserResponseDto> registerUser(@Valid @RequestBody UserRequestDto request){
        UserResponseDto response = authService.createUser(request, passwordEncoder);
        return new ApiResponse<>(true, "회원가입이 정상적으로 완료되었습니다.", response);
    }

    @GetMapping("/check/username")
    public ApiResponse<String> checkUsername(@RequestParam("username") String username){
        authService.checkUsableUsername(username);
        return new ApiResponse<>(true, "사용 가능한 아이디입니다.", username);
    }

    @GetMapping("/check/nickname")
    public ApiResponse<String> checkNickname(@RequestParam("nickname") String nickname){
        authService.checkUsableNickname(nickname);
        return new ApiResponse<>(true, "사용 가능한 닉네임입니다.", nickname);
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponseDto> login(@RequestBody LoginRequest request) {
        var response = authService.login(request.username(), request.password());
        return new ApiResponse<>(true, "로그인에 성공했습니다.", new LoginResponseDto(response.accessToken(), response.refreshToken()));
    }

    @PostMapping("/refresh")
    public ApiResponse<LoginResponseDto> refresh(@RequestBody RefreshRequest request){
        var response = authService.refresh(request.refreshToken());
        return new ApiResponse<>(true, "success", new LoginResponseDto(response.accessToken(), response.refreshToken()));
    }

    @PostMapping("/logout")
    public ApiResponse<String> logout(@RequestBody RefreshRequest request){
        authService.logout(request.refreshToken());
        return new ApiResponse<>(true, "로그아웃 되었습니다.");
    }

    public record LoginRequest(String username, String password){}
    public record RefreshRequest(String refreshToken) {}
    public record LoginResponseDto(String accessToken, String refreshToken){}
}
