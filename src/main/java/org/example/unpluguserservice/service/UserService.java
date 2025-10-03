package org.example.unpluguserservice.service;

import lombok.RequiredArgsConstructor;
import org.example.unpluguserservice.dto.UserRequestDto;
import org.example.unpluguserservice.dto.UserResponseDto;
import org.example.unpluguserservice.entity.User;
import org.example.unpluguserservice.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

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
}
