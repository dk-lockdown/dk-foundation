package com.dk.foundation.annotation;

import org.springframework.context.annotation.ComponentScan;

/**
 * Created by duguk on 2018/1/5.
 */
@ComponentScan(basePackages = "com.dk.foundation.common,com.dk.foundation.engine,com.**.mapper,com.**.service,com.**.worker")
public class EngineConfig {
}
