package io.soffa.foundation.core.models;

import lombok.Getter;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

@Getter
public final class Result<T> {

    private T data;
    private boolean success = true;
    private String error;
    private String errorDetails;
    private String tenant;
    private final List<SideEffect> effects = new ArrayList<>();

    private Result() {
    }

    public Result(String tenant, boolean success, T data, String error, String errorDetails) {
        this.data = data;
        this.success = success;
        this.error = error;
        this.errorDetails = errorDetails;
        this.tenant = tenant;
    }

    public static <T> Result<T> of(T data) {
        return new Result<>(null, true,  data, null, null);
    }

    public static <T> Result<T> failed(T data) {
        return new Result<>(null, false, data, null, null);
    }

    public static <T> Result<T> failed(String error) {
        return new Result<>(null, false, null, error, null);
    }

    public static <T> Result<T> of(Exception e) {
        return new Result<>(null, false, null,  e.getMessage(), null);
    }

    public static <T> Result<T> of(String tenant, T data) {
        return new Result<>(tenant, true, data, null, null);
    }

    public Result<T> add(@NonNull SideEffect effect) {
        effects.add(effect);
        return this;
    }

    public Result<T> add(@NonNull SideEffects effects) {
        this.effects.addAll(effects.getEffects());
        return this;
    }

}
