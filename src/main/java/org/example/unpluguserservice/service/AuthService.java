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
    public void checkUsableUsername(String username){
        if(userRepository.findByUsername(username).isPresent()){
            throw new RuntimeException("이미 존재하는 아이디입니다.");
        }
    }

    @Transactional(readOnly = true)
    public void checkUsableNickname(String nickname){
        if (userRepository.findByNickname(nickname).isPresent()){
            throw new RuntimeException("이미 존재하는 닉네임입니다.");
        }
    }

    public LoginResponse login(String username, String password){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (!passwordEncoder.matches(password, user.getPassword())){
            throw new IllegalArgumentException("비밀번호가 올바르지 않습니다.");
        }

        String accessToken = jwtUtil.generateAccessToken(user.getUsername());
        String refreshToken = jwtUtil.generateRefreshToken(user.getUsername());

        RefreshToken r = new RefreshToken(null, refreshToken, user.getUsername(), new Date(System.currentTimeMillis() + refreshExpMs));
        refreshTokenRepository.save(r);

        return new LoginResponse(accessToken, refreshToken);
    }

    public LoginResponse refresh(String refreshToken){
        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Invalid refresh token"));

        if (token.getExpiryDate().before(new Date())){
            refreshTokenRepository.delete(token);
            throw new IllegalArgumentException("Refresh Token expired");
        }

        User user = userRepository.findByUsername(token.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        String newAccess = jwtUtil.generateAccessToken(user.getUsername());
        return new LoginResponse(newAccess, refreshToken);
    }

    public void logout(String refreshToken){
        refreshTokenRepository.findByToken(refreshToken).ifPresent(refreshTokenRepository::delete);
    }

    public static record LoginResponse(String accessToken, String refreshToken){}
}
