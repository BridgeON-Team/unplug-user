package org.example.unpluguserservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.unpluguserservice.common.ApiResponse;
import org.example.unpluguserservice.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

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
