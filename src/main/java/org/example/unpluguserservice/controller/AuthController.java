package org.example.unpluguserservice.controller;

import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "회원가입")
    public ApiResponse<UserResponseDto> registerUser(@Valid @RequestBody UserRequestDto request){
        UserResponseDto response = authService.createUser(request, passwordEncoder);
        if (response == null){
            return new ApiResponse<>(false, "회원가입에 실패했습니다.", null);
        }
        return new ApiResponse<>(true, "회원가입이 정상적으로 완료되었습니다.", response);
    }

    @GetMapping("/check/username")
    @Operation(summary = "아이디 중복 확인")
    public ApiResponse<String> checkUsername(@RequestParam("username") String username){
        if (authService.checkUsableUsername(username)){
            return new ApiResponse<>(true, "사용 가능한 아이디입니다.", username);
        }else {
            return new ApiResponse<>(false, "사용할 수 없는 아이디입니다.", username);
        }
    }

    @GetMapping("/check/nickname")
    @Operation(summary = "닉네임 중복 확인")
    public ApiResponse<String> checkNickname(@RequestParam("nickname") String nickname){
        if (authService.checkUsableNickname(nickname)) {
            return new ApiResponse<>(true, "사용 가능한 닉네임입니다.", nickname);
        }else {
            return new ApiResponse<>(false, "사용할 수 없는 닉네임입니다.", nickname);
        }
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "성공 시 accesstoken 리턴")
    public ApiResponse<LoginResponseDto> login(@RequestBody LoginRequest request) {
        var response = authService.login(request.username(), request.password());
        if (response.accessToken() == null){
            return new ApiResponse<>(false, response.data(), new LoginResponseDto(response.data(), null, null));
        }
        return new ApiResponse<>(true, "로그인에 성공했습니다.", new LoginResponseDto(response.data(), response.accessToken(), response.refreshToken()));
    }

    @PostMapping("/refresh")
    @Operation(summary = "토큰 refresh")
    public ApiResponse<LoginResponseDto> refresh(@RequestBody RefreshRequest request){
        var response = authService.refresh(request.refreshToken());
        if (response.accessToken() == null){
            return new ApiResponse<>(false, response.data(), new LoginResponseDto(response.data(), null, response.refreshToken()));
        }
        return new ApiResponse<>(true, "success", new LoginResponseDto(response.data(), response.accessToken(), response.refreshToken()));
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃")
    public ApiResponse<String> logout(@RequestBody RefreshRequest request){
        authService.logout(request.refreshToken());
        return new ApiResponse<>(true, "로그아웃 되었습니다.");
    }

    public record LoginRequest(String username, String password){}
    public record RefreshRequest(String refreshToken) {}
    public record LoginResponseDto(String data, String accessToken, String refreshToken){}
}
