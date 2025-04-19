package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.LOCKED)
public class RedisLockException extends RuntimeException {

    public RedisLockException(String message) {
        super(message);
    }
}
