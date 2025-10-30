package org.example.unpluguserservice.service;

import lombok.RequiredArgsConstructor;
import org.example.unpluguserservice.dto.UserRequestDto;
import org.example.unpluguserservice.dto.UserResponseDto;
import org.example.unpluguserservice.entity.RefreshToken;
import org.example.unpluguserservice.entity.User;
import org.example.unpluguserservice.repository.RefreshTokenRepository;
import org.example.unpluguserservice.repository.UserRepository;
import org.example.unpluguserservice.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.refresh-token-expiration-ms}")
    private long refreshExpMs;

    @Transactional
    public UserResponseDto createUser(UserRequestDto request, PasswordEncoder passwordEncoder){
        if(userRepository.findByUsername(request.getUsername()).isPresent()){
            return null;
        }
        User user = User.builder()
                .username(request.getUsername())
                .name(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .createdDate(LocalDateTime.now())
                .build();
        User savedUser = userRepository.save(user);
        return UserResponseDto.fromEntity(savedUser);
    }

    @Transactional(readOnly = true)
    public boolean checkUsableUsername(String username){
        return userRepository.findByUsername(username).isEmpty();
    }

    @Transactional(readOnly = true)
    public boolean checkUsableNickname(String nickname){
        return userRepository.findByNickname(nickname).isEmpty();
    }

    public LoginResponse login(String username, String password){
        if(userRepository.findByUsername(username).isEmpty()){
            return new LoginResponse("아이디가 올바르지 않습니다.", null, null);
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (!passwordEncoder.matches(password, user.getPassword())){
            return new LoginResponse("비밀번호가 올바르지 않습니다.", null, null);
        }

        String accessToken = jwtUtil.generateAccessToken(user.getUsername());
        String refreshToken = jwtUtil.generateRefreshToken(user.getUsername());

        RefreshToken r = new RefreshToken(null, refreshToken, user.getUsername(), new Date(System.currentTimeMillis() + refreshExpMs));
        refreshTokenRepository.save(r);

        return new LoginResponse("로그인에 성공했습니다.", accessToken, refreshToken);
    }

    public LoginResponse refresh(String refreshToken){
        if (refreshTokenRepository.findByToken(refreshToken).isEmpty()){
            return new LoginResponse("Invalid Refresh Token ", null, refreshToken);
        }
        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Invalid refresh token"));

        if (token.getExpiryDate().before(new Date())){
            refreshTokenRepository.delete(token);
            return new LoginResponse("Refresh Token expired", null, refreshToken);
        }

        User user = userRepository.findByUsername(token.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        String newAccess = jwtUtil.generateAccessToken(user.getUsername());
        return new LoginResponse("success", newAccess, refreshToken);
    }

    public void logout(String refreshToken){
        refreshTokenRepository.findByToken(refreshToken).ifPresent(refreshTokenRepository::delete);
    }

    public static record LoginResponse(String data, String accessToken, String refreshToken){}
}
