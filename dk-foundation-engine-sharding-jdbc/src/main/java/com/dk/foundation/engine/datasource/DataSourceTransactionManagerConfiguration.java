package com.dk.foundation.engine.datasource;

import com.dk.foundation.engine.springcontext.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * Created by duguk on 2018/1/9.
 */
@Configuration
@EnableTransactionManagement
public class DataSourceTransactionManagerConfiguration extends DataSourceTransactionManagerAutoConfiguration {
    final static Logger logger = LoggerFactory.getLogger(DataSourceTransactionManagerConfiguration.class);
    /**
     * 自定义事务
     * MyBatis自动参与到spring事务管理中，无需额外配置，只要org.mybatis.spring.SqlSessionFactoryBean引用的数据源与DataSourceTransactionManager引用的数据源一致即可，否则事务管理会不起作用。
     * @return
     */
    @Primary
    @Bean(name = "myTransactionManager")
    @Resource
    public DataSourceTransactionManager transactionManagers(DataSource myBatisDataSource) {
        logger.info("-------------------- transactionManager init ---------------------");
        return new DataSourceTransactionManager(myBatisDataSource);
    }
}
