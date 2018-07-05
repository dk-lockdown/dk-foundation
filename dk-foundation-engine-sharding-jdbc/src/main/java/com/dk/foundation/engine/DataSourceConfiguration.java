package com.dk.foundation.engine;

import io.shardingjdbc.core.api.MasterSlaveDataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by duguk on 2018/1/8.
 */
@Configuration
public class DataSourceConfiguration {
    final static Logger logger = LoggerFactory.getLogger(DataSourceConfiguration.class);

    @Value("${dataSource.useMasterSlave:false}")
    private boolean useMasterSlaveDataSource;

    @Bean(name = "myBatisDataSource")
    @Primary
    @ConfigurationProperties(prefix = "dataSource.singleDB")
    public DataSource userDataSource() throws IOException, SQLException {
        if(!useMasterSlaveDataSource) {
            logger.info("-------------------- singleDB DataSource init ---------------------");
            return DataSourceBuilder.create().build();
        }else {
            logger.info("-------------------- masterSlave DataSource init ---------------------");
            DataSource dataSource = MasterSlaveDataSourceFactory.createDataSource(new File(
                    DataSourceConfiguration.class.getResource("/META-INF/yamlMasterSlave.yaml").getFile()));
            return dataSource;
        }
    }

}
