package com.petcommunity.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    private int code;
    private String message;
    private T data;

    public static <T> ApiResponse<T> ok(T data) {
        return ApiResponse.<T>builder().code(200).message("success").data(data).build();
    }

    public static <T> ApiResponse<T> ok(String msg, T data) {
        return ApiResponse.<T>builder().code(200).message(msg).data(data).build();
    }

    public static <T> ApiResponse<T> error(int code, String msg) {
        return ApiResponse.<T>builder().code(code).message(msg).data(null).build();
    }
}
