package com.example.cpu11398_local.cleanarchitecturedemo.data.helper;

/**
 * Created by Hung-pc on 7/8/2018.
 * Optional is used to wrapper data
 */

public final class Optional<T> {

    private final T value;

    private Optional(T value) {
        this.value = value;
    }

    public static <T> Optional<T> empty() {
        return new Optional<T>(null);
    }

    public static <T> Optional<T> of(T value){
        if (value == null) {
            return Optional.empty();
        }
        return new Optional<T>(value);
    }

    public boolean isPresent() {
        return value != null;
    }

    public T get() {
        return value;
    }
}