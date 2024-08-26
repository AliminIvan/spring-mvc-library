package com.ivanalimin.spring_mvc_library.exception_handling;

import org.springframework.lang.NonNull;

public class AppException extends RuntimeException {
    public AppException(@NonNull String message) {
        super(message);
    }
}
