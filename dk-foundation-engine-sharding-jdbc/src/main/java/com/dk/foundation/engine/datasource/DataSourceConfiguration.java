package com.dk.foundation.engine.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.base.Preconditions;
import com.dk.foundation.common.PropertyUtil;
import com.dk.foundation.engine.datasource.shardingjdbc.common.SpringBootConfigMapConfigurationProperties;
import com.dk.foundation.engine.datasource.shardingjdbc.common.SpringBootPropertiesConfigurationProperties;
import com.dk.foundation.engine.datasource.shardingjdbc.masterslave.SpringBootMasterSlaveRuleConfigurationProperties;
import com.dk.foundation.engine.datasource.shardingjdbc.sharding.SpringBootShardingRuleConfigurationProperties;
import io.shardingsphere.shardingjdbc.api.MasterSlaveDataSourceFactory;
import io.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by duguk on 2018/1/8.
 */
@Configuration
@EnableConfigurationProperties({
        SpringBootShardingRuleConfigurationProperties.class, SpringBootMasterSlaveRuleConfigurationProperties.class,
        SpringBootConfigMapConfigurationProperties.class, SpringBootPropertiesConfigurationProperties.class
})
@RequiredArgsConstructor
public class DataSourceConfiguration implements EnvironmentAware {
    final static Logger logger = LoggerFactory.getLogger(DataSourceConfiguration.class);

    private final SpringBootShardingRuleConfigurationProperties shardingProperties;

    private final SpringBootMasterSlaveRuleConfigurationProperties masterSlaveProperties;

    private final SpringBootConfigMapConfigurationProperties configMapProperties;

    private final SpringBootPropertiesConfigurationProperties propMapProperties;

    private final Map<String, DataSource> dataSourceMap = new LinkedHashMap<>();

    @Value("${data-source.useMasterSlave:false}")
    private boolean useMasterSlaveDataSource;

    @Bean(name = "myBatisDataSource")
    @Primary
    @ConfigurationProperties(prefix = "data-source.single-db")
    public DataSource userDataSource() throws IOException, SQLException {
        if(!useMasterSlaveDataSource) {
            logger.info("-------------------- singleDB DataSource init ---------------------");
            return DataSourceBuilder.create().build();
        }else {
            logger.info("-------------------- masterSlave DataSource init ---------------------");
//            DataSource dataSource = YamlMasterSlaveDataSourceFactory.createDataSource(new File(
//                    DataSourceConfiguration.class.getResource("/META-INF/yamlMasterSlave.yaml").getFile()));
//            return dataSource;
            return null == masterSlaveProperties.getMasterDataSourceName()
                    ? ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingProperties.getShardingRuleConfiguration(), configMapProperties.getConfigMap(), propMapProperties.getProps())
                    : MasterSlaveDataSourceFactory.createDataSource(
                    dataSourceMap, masterSlaveProperties.getMasterSlaveRuleConfiguration(), configMapProperties.getConfigMap(), propMapProperties.getProps());
        }
    }

    @Override
    public final void setEnvironment(final Environment environment) {
        if(useMasterSlaveDataSource) {
            setDataSourceMap(environment);
        }
    }

    @SuppressWarnings("unchecked")
    private void setDataSourceMap(final Environment environment) {
        String prefix = "sharding.jdbc.datasource.";
        String dataSources = environment.getProperty(prefix + "names");
        for (String each : dataSources.split(",")) {
            Map<String, Object> dataSourceProps = PropertyUtil.handle(environment, prefix + each.trim(), Map.class);
            Preconditions.checkState(!dataSourceProps.isEmpty(), "Wrong datasource properties!");
            DruidDataSource dataSource = initDataSource(dataSourceProps.get("url").toString(), dataSourceProps.get("username").toString(), dataSourceProps.get("password").toString());
            dataSourceMap.put(each, dataSource);
        }
    }

    public static DruidDataSource initDataSource(String url, String username, String password) {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl(url);
        druidDataSource.setUsername(username);
        druidDataSource.setPassword(password);

        try {
            druidDataSource.setFilters("stat");
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("druid filter init failed",e);
        }

        druidDataSource.setMaxActive(20);
        druidDataSource.setInitialSize(1);
        druidDataSource.setMaxWait(60000);
        druidDataSource.setMinIdle(1);

        druidDataSource.setTimeBetweenEvictionRunsMillis(60000);
        druidDataSource.setMinEvictableIdleTimeMillis(300000);

        druidDataSource.setValidationQuery("select 'x'");
        druidDataSource.setTestWhileIdle(true);
        druidDataSource.setTestOnBorrow(false);
        druidDataSource.setTestOnReturn(false);
        druidDataSource.setPoolPreparedStatements(true);
        druidDataSource.setMaxOpenPreparedStatements(20);
        druidDataSource.setKeepAlive(true);
        druidDataSource.setRemoveAbandoned(true);

        return druidDataSource;
    }
}
