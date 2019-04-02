package com.dk.foundation.annotation;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Created by duguk on 2018/1/5.
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(EngineConfig.class)
@SpringBootApplication
@EnableAutoConfiguration
public @interface EnableEngineStart {
    String[] value()  default {};
    String[] basePackages() default {};
    Class<?>[] basePackageClasses() default {};
}
