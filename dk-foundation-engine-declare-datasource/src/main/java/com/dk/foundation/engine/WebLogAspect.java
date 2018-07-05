package com.dk.foundation.engine;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * Created by duguk on 2018/1/9.
 */
@Aspect
@Component
public class WebLogAspect {
    final static Logger logger = LoggerFactory.getLogger(WebLogAspect.class);

    public static final String RESPONSE_NAME_AT_ATTRIBUTES = ServletRequestAttributes.class.getName()
            + ".ATTRIBUTE_NAME";
    @Value("${system.model:prod}")
    private String system_mode;

    @Pointcut("execution(public * com.dk.*..modules..*.controller..*.*(..)) ")
    public void webLog() {
    }

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }
        HttpServletRequest request = attributes.getRequest();

        // 记录下请求内容
        logger.info("URL : " + request.getRemoteAddr() + ":" + request.getMethod() + "->"
                + request.getRequestURL().toString());
        logger.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "."
                + joinPoint.getSignature().getName());
        logger.info("ARGS : " + Arrays.toString(joinPoint.getArgs()));

    }

    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) throws Throwable {
        if (logger.isInfoEnabled()) {
            logger.debug("system_mode:"+system_mode);
            if (system_mode.trim().toLowerCase().equals("prod") && ret instanceof String) {
                String resp = ret.toString();
                try {
                    int bodyindex =resp.indexOf("\"body\":");
                    if(bodyindex>0) {
                        int errorindex =resp.indexOf(",\"errorMessage\"");
                        resp = resp.substring(0, bodyindex)+"..."+(errorindex>0?resp.substring(errorindex):"");
                    }
                    logger.info("RESPONSE : " + resp);
                } catch (Exception e) {
                    logger.info("RESPONSE : " + ret);
                }
            } else {
                logger.info("RESPONSE : " + ret);
            }
        }
    }
}