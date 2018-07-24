package com.dk.foundation.common;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.stereotype.Component;

/**
 * Created by duguk on 2018/2/1.
 */
@Aspect
@Component
public class RedisAspect {
    final static Logger logger = LoggerFactory.getLogger(RedisAspect.class);

    @Around("execution(* com.dk.foundation.common.RedisUtils.*(..))")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object result = null;
        try{
            result = point.proceed();
        }catch (Exception e) {
            logger.error("redis error", e);
            throw new RedisSystemException("Redis服务异常", e);
        }
        return result;
    }
}