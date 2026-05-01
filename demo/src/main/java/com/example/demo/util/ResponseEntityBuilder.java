package com.example.demo.util;

import com.example.demo.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseEntityBuilder<T> {
    private final int code;
    private String message;
    private T data;

    private ResponseEntityBuilder(HttpStatus status) {
        this.code = status.value();
    }

    // 修正：靜態方法必須自帶泛型宣告 <T>
    public static <T> ResponseEntityBuilder<T> success() {
        return new ResponseEntityBuilder<>(HttpStatus.OK);
    }

    public static <T> ResponseEntityBuilder<T> error() {
        return new ResponseEntityBuilder<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntityBuilder<T> message(String message) {
        this.message = message;
        return this;
    }

    public ResponseEntityBuilder<T> data(T data) {
        this.data = data;
        return this;
    }

    public ResponseEntity<ApiResponse<T>> build() {
        // 這裡要確保 ApiResponse 已經在 dto package 定義好
        return ResponseEntity.status(code).body(new ApiResponse<>(code, message, data));
    }
}