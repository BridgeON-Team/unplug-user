package org.example.unpluguserservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.unpluguserservice.entity.User;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private Long userId;
    private String username;
    private String name;
    private String nickname;
    private String profileImgUrl;

    public static UserResponseDto fromEntity(User user){
        return UserResponseDto.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .name(user.getName())
                .nickname(user.getNickname())
                .profileImgUrl(user.getProfileImgUrl())
                .build();
    }
}
