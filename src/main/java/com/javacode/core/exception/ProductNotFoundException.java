package com.javacode.core.exception;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(Long id) {
        super("Продукт с айДИ " + id + " не найден!");
    }
}