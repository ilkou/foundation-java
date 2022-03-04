package io.soffa.foundation.core.models;

import lombok.Data;

@Data
public class Response<T> {

    private boolean success;
    private String message;
    private String error;
    private T data;

    public Response(boolean success, String message, String error, T data) {
        this.success = success;
        this.message = message;
        this.error = error;
        this.data = data;
    }

    public static <T> Response<T> ok() {
        return ok(null);
    }

    public static <T> Response<T> ok(T data) {
        return new Response<>(true, null, null, data);
    }

    public static <T> Response<T> failed(String error) {
        return new Response<>(false, null, error, null);
    }

}
