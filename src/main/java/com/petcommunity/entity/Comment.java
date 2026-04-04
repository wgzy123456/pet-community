package com.petcommunity.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long postId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 500)
    private String content;

    @Builder.Default
    private LocalDateTime createTime = LocalDateTime.now();

    /** 非持久化字段：评论者用户名 */
    @Transient
    private String username;

    /** 非持久化字段：评论者头像 */
    @Transient
    private String userAvatar;
}
