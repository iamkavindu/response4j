package io.github.iamkavindu.response4j.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.Map;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private int status;

    private String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant timestamp;

    private T data;

    public static <T> ApiResponse<T> of(int status, String message, T data) {
        return ApiResponse.<T>builder()
                .status(status)
                .message(message)
                .timestamp(Instant.now())
                .data(data)
                .build();
    }

    @SuppressWarnings("unchecked")
    public static <T> ApiResponse<T> empty(int status, String message) {
        return ApiResponse.<T>builder()
                .status(status)
                .message(message)
                .timestamp(Instant.now())
                .data((T) Map.of())
                .build();
    }

    public static <T> ApiResponse<T> ok(T data) {
        return of(200, "Request successful", data);
    }

    public static <T> ApiResponse<T> created(T data) {
        return of(201, "Request created successful", data);
    }

    public static <T> ApiResponse<T> noContent() {
        return empty(204, "Request created successful");
    }
}
