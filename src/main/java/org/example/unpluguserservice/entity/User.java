package org.example.unpluguserservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    // 유저 식별키 (시퀀스용)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long userId;

    // 유저 아이디
    @Column(name = "username", nullable = false)
    private String username;

    // 유저 이름
    @Column(name = "name", nullable = true)
    private String name;

    @Column(name = "password", nullable = false)
    private String password;

    // 유저 닉네임
    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "created_date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdDate;

    @Column(name = "profile_img_url")
    private String profileImgUrl;
}
