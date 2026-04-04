package com.petcommunity.dto;

import lombok.Data;

@Data
public class PostRequest {
    private Long userId;
    private String content;
    private String images;
    private String topic;
    private String location;
}
