package org.example.qpin.global.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@Builder
public class ResponseDto<T> {

    private int code;
    private boolean inSuccess;
    private String message;
    private T result;

    public ResponseDto(int code, boolean inSuccess, String message) {
        this.code = code;
        this.inSuccess = inSuccess;
        this.message = message;
        this.result = null;
    }

    public static<T> ResponseDto<T> response(int code, boolean inSuccess, String message) {
        return response(code, inSuccess, message, null);
    }


    public static<T> ResponseDto<T> response(int code, boolean inSuccess, String message, T t) {
        return ResponseDto.<T>builder()
                .code(code)
                .inSuccess(inSuccess)
                .message(message)
                .result(t)
                .build();
    }
}
