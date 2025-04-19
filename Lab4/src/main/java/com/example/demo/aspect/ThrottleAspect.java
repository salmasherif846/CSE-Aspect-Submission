package com.example.demo.aspect;

import com.example.demo.annotation.Throttle;
import com.example.demo.exception.TooManyRequestsException;
import com.example.demo.service.RedisService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.Duration;


@Aspect
@Component
@Order(1) // Ensures this aspect runs before others
public class ThrottleAspect {

    @Autowired
    private RedisService redisService;

    @Autowired
    private HttpServletRequest httpRequest;

    @Around("@annotation(throttle)")
    public Object applyRateThrottle(ProceedingJoinPoint joinPoint, Throttle throttle) throws Throwable {
        String clientIp = httpRequest.getRemoteAddr();
        String redisKey = "throttle:" + throttle.cacheKeyPrefix() + ":" + clientIp;
        long windowSeconds = throttle.unit().toSeconds(throttle.window());

        Long hitCount = redisService.incrementAndSetExpiry(redisKey, Duration.ofSeconds(windowSeconds));

        if (hitCount != null && hitCount > throttle.maxCalls()) {
            throw new TooManyRequestsException("Rate limit exceeded. Please try again later.");
        }

        return joinPoint.proceed();
    }
}
