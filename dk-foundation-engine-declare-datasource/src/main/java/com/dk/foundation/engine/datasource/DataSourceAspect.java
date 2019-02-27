package com.dk.foundation.engine.datasource;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class DataSourceAspect implements Ordered {
    protected Logger logger = LoggerFactory.getLogger(DataSourceAspect.class);

    @Pointcut("@annotation(com.dk.foundation.engine.datasource.DataSource)")
    public void dataSourcePointCut() {

    }

    @Around("dataSourcePointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        DataSource ds = method.getAnnotation(DataSource.class);
        if(ds == null){
            DynamicDataSourceKey.setDataSource(DynamicDataSourceKey.MASTER);
            logger.info("set datasource is " + DynamicDataSourceKey.MASTER);
        }else {
            if(!StringUtils.isBlank(ds.name())) {
                DynamicDataSourceKey.setDataSource(ds.name());
                logger.info("set datasource is " + ds.name());
            }
            else
            {
                if(ds.readRandom())
                {
                    DynamicDataSourceKey.setDataSource(DynamicDataSourceKey.READ_RANDOM_PROXY);
                    logger.info("set datasource is " + DynamicDataSourceKey.READ_RANDOM_PROXY);
                }
                else {
                    DynamicDataSourceKey.setDataSource(DynamicDataSourceKey.MASTER);
                    logger.info("set datasource is " + DynamicDataSourceKey.MASTER);
                }
            }
        }

        try {
            return point.proceed();
        } finally {
            DynamicDataSourceKey.clearDataSource();
            logger.info("clean datasource");
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
