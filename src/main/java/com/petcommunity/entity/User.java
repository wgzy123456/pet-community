package com.petcommunity.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(length = 100)
    private String nickname;

    @Column(length = 500)
    private String avatar;

    @Column(length = 20)
    private String city;

    @Column(length = 20)
    private String phone;

    @Column(length = 100)
    private String email;

    @Builder.Default
    private Integer level = 1;

    @Builder.Default
    private Integer followersCount = 0;

    @Builder.Default
    private Integer followingCount = 0;

    @Builder.Default
    private Integer postsCount = 0;

    @Builder.Default
    private Integer likesCount = 0;

    @Builder.Default
    private LocalDateTime createTime = LocalDateTime.now();

    private LocalDateTime updateTime;

    @PreUpdate
    public void preUpdate() {
        this.updateTime = LocalDateTime.now();
    }
}
