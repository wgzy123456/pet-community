package com.petcommunity.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    /** 内容文字 */
    @Column(length = 2000)
    private String content;

    /** 图片URL列表，逗号分隔 */
    @Column(length = 2000)
    private String images;

    /** 话题标签 */
    @Column(length = 100)
    private String topic;

    /** 点赞数 */
    @Builder.Default
    private Integer likesCount = 0;

    /** 评论数 */
    @Builder.Default
    private Integer commentsCount = 0;

    /** 位置 */
    @Column(length = 50)
    private String location;

    @Builder.Default
    private LocalDateTime createTime = LocalDateTime.now();

    private LocalDateTime updateTime;

    /** 非持久化字段：是否已点赞 */
    @Transient
    private Boolean liked = false;

    /** 非持久化字段：用户名 */
    @Transient
    private String username;

    /** 非持久化字段：用户头像 */
    @Transient
    private String userAvatar;
}
