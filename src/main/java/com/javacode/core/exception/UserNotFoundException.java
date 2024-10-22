package com.javacode.core.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id) {
        super("Юзер с айДИ " + id + " не найден!");
    }
}
