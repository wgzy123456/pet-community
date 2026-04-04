package com.petcommunity.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Entity
@Table(name = "pets")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 30)
    private String name;

    /** 宠物类型：cat / dog / other */
    @Column(nullable = false, length = 20)
    private String type;

    /** 品种 */
    @Column(length = 50)
    private String breed;

    /** 性别：male / female / unknown */
    @Column(length = 10)
    private String gender;

    /** 出生日期 */
    private LocalDate birthDate;

    /** 年龄（岁） */
    private Integer age;

    /** 头像图片URL */
    @Column(length = 500)
    private String avatar;

    /** 简介 */
    @Column(length = 200)
    private String bio;

    @Builder.Default
    private LocalDateTime createTime = LocalDateTime.now();
}
