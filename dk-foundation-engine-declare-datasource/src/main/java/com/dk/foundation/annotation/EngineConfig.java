package com.dk.foundation.annotation;

import org.springframework.context.annotation.ComponentScan;

/**
 * Created by duguk on 2018/1/5.
 */
@ComponentScan(basePackages = "com.dk.foundation.common,com.dk.foundation.engine,com.dk.*.mapper,com.dk.*.service,com.dk.*.worker")
public class EngineConfig {
}
