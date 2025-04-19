package com.example.demo.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;


@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisLock {

    // A prefix to distinguish the lock key in Redis
    String prefix();

    // SpEL expression to extract a dynamic part of the key (e.g., method arg)
    String key();

    // Duration to hold the lock before it expires
    long duration() default 30;

    // Time unit for the lock duration
    TimeUnit unit() default TimeUnit.SECONDS;
}
