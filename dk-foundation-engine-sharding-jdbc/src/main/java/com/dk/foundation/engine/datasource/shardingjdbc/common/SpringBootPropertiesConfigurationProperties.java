/*
 * Copyright 2016-2018 shardingsphere.io.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package com.dk.foundation.engine.datasource.shardingjdbc.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Properties;

/**
 * Properties configuration properties.
 *
 * @author zhangliang
 */
@ConfigurationProperties(prefix = "sharding.jdbc.config")
@Getter
@Setter
public class SpringBootPropertiesConfigurationProperties {
    
    private Properties props = new Properties();
}
