package com.dk.foundation.engine.datasource;

import com.dk.foundation.engine.springcontext.SpringContextHolder;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.List;

/**
 * Created by duguk on 2018/1/8.
 */
@Configuration
@EnableConfigurationProperties({MybatisProperties.class})
public class MybatisConfiguration extends MybatisAutoConfiguration{
    final static Logger logger = LoggerFactory.getLogger(MybatisConfiguration.class);

    public MybatisConfiguration(MybatisProperties properties, ObjectProvider<Interceptor[]> interceptorsProvider, ResourceLoader resourceLoader, ObjectProvider<DatabaseIdProvider> databaseIdProvider, ObjectProvider<List<ConfigurationCustomizer>> configurationCustomizersProvider) {
        super(properties, interceptorsProvider, resourceLoader, databaseIdProvider, configurationCustomizersProvider);
    }

    @Bean
    @Resource
    @Override
    public SqlSessionFactory sqlSessionFactory(DataSource myBatisDataSource) throws Exception {
        logger.info("-------------------- sqlSessionFactory init ---------------------");
        return super.sqlSessionFactory(myBatisDataSource);
    }
}
