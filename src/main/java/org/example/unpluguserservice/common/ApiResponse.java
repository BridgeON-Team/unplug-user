package org.example.unpluguserservice.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;

    // 객체 반환 없이 성공 여부만 반환
    public  ApiResponse(boolean success, String message){
        this.success = success;
        this.message = message;
    }

    // 객체와 성공 여부 반환
    public ApiResponse(boolean success, String message, T data){
        this.success = success;
        this.message = message;
        this.data = data;
    }
}
