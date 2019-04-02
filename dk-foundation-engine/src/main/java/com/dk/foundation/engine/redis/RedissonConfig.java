package com.dk.foundation.engine.redis;

import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Configuration
@AutoConfigureAfter({ RedisProperties.class})
@ConditionalOnClass(RedisProperties.class)
public class RedissonConfig {

    @Autowired
    RedisProperties redisProperties;

    @Bean
    @Primary
    @ConditionalOnProperty(name="spring.redis.sentinel.master")
    RedissonClient redissonSentinel() {
        Config config = new Config();
        List<String> sentinels= redisProperties.getSentinel().getNodes();
        SentinelServersConfig serverConfig = config.useSentinelServers()
                .setMasterName(redisProperties.getSentinel().getMaster())
                .addSentinelAddress(sentinels.toArray(new String[sentinels.size()]))
                .setDatabase(redisProperties.getDatabase());
        if(StringUtils.isNotBlank(redisProperties.getPassword())) {
            serverConfig.setPassword(redisProperties.getPassword());
        }
        if (redisProperties.getJedis().getPool() != null) {
            serverConfig.setTimeout((int) redisProperties.getTimeout().getSeconds())
                    .setMasterConnectionPoolSize(redisProperties.getJedis().getPool().getMaxActive())
                    .setMasterConnectionMinimumIdleSize(redisProperties.getJedis().getPool().getMinIdle())
                    .setSlaveConnectionPoolSize(redisProperties.getJedis().getPool().getMaxActive())
                    .setSlaveConnectionMinimumIdleSize(redisProperties.getJedis().getPool().getMinIdle());
        }
        return Redisson.create(config);
    }

    @Bean
    @ConditionalOnProperty(name="spring.redis.url")
    RedissonClient redissonSingle() {
        Config config = new Config();
        SingleServerConfig serverConfig = config.useSingleServer()
                .setAddress(redisProperties.getUrl())
                .setDatabase(redisProperties.getDatabase());
        if (StringUtils.isNotBlank(redisProperties.getPassword())) {
            serverConfig.setPassword(redisProperties.getPassword());
        }
        if (redisProperties.getTimeout() != null) {
            serverConfig.setTimeout((int) redisProperties.getTimeout().getSeconds());
        }
        if (redisProperties.getJedis().getPool() != null) {
            serverConfig.setConnectionPoolSize(redisProperties.getJedis().getPool().getMaxActive())
                    .setConnectionMinimumIdleSize(redisProperties.getJedis().getPool().getMinIdle());
        }

        return Redisson.create(config);
    }
}