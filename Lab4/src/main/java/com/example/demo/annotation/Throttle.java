package com.example.demo.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;


@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Throttle {

    // Time window duration
    long window();

    // Maximum number of allowed method invocations
    long maxCalls();

    // Unit of the time window
    TimeUnit unit() default TimeUnit.SECONDS;

    // Optional prefix for the Redis key
    String cacheKeyPrefix() default "";
}
