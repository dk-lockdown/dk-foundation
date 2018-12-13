package com.dk.foundation.engine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
/**
 * Created by duguk on 2018/1/9.
 */
@Configuration
@AutoConfigureAfter({ SpringContextHolder.class,DataSourceConfiguration.class})
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
    public DataSourceTransactionManager transactionManagers() {
        logger.info("-------------------- transactionManager init ---------------------");
        return new DynamicDataSourceTransactionManager(SpringContextHolder.getBean("myBatisDataSource"));
    }
}
