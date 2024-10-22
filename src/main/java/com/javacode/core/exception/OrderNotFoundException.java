package com.javacode.core.exception;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(Long id) {
        super("Заказ с айДИ " + id + " не найден!");
    }
}