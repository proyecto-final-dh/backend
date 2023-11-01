package com.company.utils;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@JsonPropertyOrder({ "httpHeaders", "httpStatusCode", "message", "data", "errors" ,"otherParams" })
public class ApiResponse<T, S> {

    private final int httpStatusCode;
    private final String message;
    private final T data;

    private final S errors;

    private ApiResponse(ApiResponseBuilder builder) {
        this.httpStatusCode = builder.httpStatusCode;
        this.message = builder.message;
        this.data = (T) builder.data;
        this.errors = (S) builder.errors;
    }


    public static class ApiResponseBuilder<T, S> {

        private final int httpStatusCode;
        private final String message;
        private T data;
        private S errors;

        public ApiResponseBuilder(int httpStatusCode, String message) {
            this.httpStatusCode = httpStatusCode;
            this.message = message;
        }

        public ApiResponseBuilder<T, S> withData(T data) {
            this.data = data;
            return this;
        }

        public ApiResponseBuilder<T, S> withErrors(S errors) {
            this.errors = errors;
            return this;
        }

        public ResponseEntity<ApiResponse<T, S>> build() {
            ApiResponse<T, S> apiResponse = new ApiResponse<>(this);
            return ResponseEntity.status(apiResponse.getHttpStatusCode()).body(apiResponse);
        }
    }
}