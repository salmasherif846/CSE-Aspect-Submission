package com.example.demo.aspect;

import com.example.demo.annotation.RedisLock;
import com.example.demo.exception.RedisLockException;
import com.example.demo.service.RedisService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.*;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.UUID;


@Aspect
@Component
public class RedisLockAspect {

    @Autowired
    private RedisService redisService;

    private final ExpressionParser expressionParser = new SpelExpressionParser();

    @Around("@annotation(redisLock)")
    public Object applyLock(ProceedingJoinPoint joinPoint, RedisLock redisLock) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        // Prepare SpEL context for dynamic key resolution
        EvaluationContext context = new StandardEvaluationContext();
        Object[] args = joinPoint.getArgs();
        String[] paramNames = methodSignature.getParameterNames();
        for (int i = 0; i < args.length; i++) {
            context.setVariable(paramNames[i], args[i]);
        }

        // Build lock key from annotation and expression
        String resolvedKey = expressionParser.parseExpression(redisLock.key()).getValue(context, String.class);
        String redisKey = "lock:" + redisLock.prefix() + ":" + resolvedKey;
        String token = UUID.randomUUID().toString();
        Duration ttl = Duration.of(redisLock.duration(), redisLock.unit().toChronoUnit());

        // Attempt to acquire lock
        boolean acquired = Boolean.TRUE.equals(redisService.acquireLock(redisKey, token, ttl));

        if (!acquired) {
            throw new RedisLockException("Could not acquire lock on resource.");
        }

        try {
            return joinPoint.proceed();
        } finally {
            redisService.remove(redisKey); // cleanup
        }
    }
}
