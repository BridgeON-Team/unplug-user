package org.example.unpluguserservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {
    private String username;
    private String name;
    private String password;
    private String nickname;
}
