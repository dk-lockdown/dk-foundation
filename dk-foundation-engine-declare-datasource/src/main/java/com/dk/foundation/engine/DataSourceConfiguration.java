package com.dk.foundation.engine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by duguk on 2018/1/8.
 */
@Configuration
public class DataSourceConfiguration {
    final static Logger logger = LoggerFactory.getLogger(DataSourceConfiguration.class);

    @Bean(name = "myBatisDataSource")
    @Primary
    public DynamicDataSource dataSource() throws IOException {
        logger.info("-------------------- dynamicDataSource init ---------------------");
        DynamicDataSourceContainer dynamicDataSourceContainer = unmarshal(new File(
                DynamicDataSourceContainer.class.getResource("/META-INF/muiltiDataSource.yaml").getFile()));
        return new DynamicDataSource(dynamicDataSourceContainer.getDataSources().get("master"), dynamicDataSourceContainer.getDataSources());
    }

    private DynamicDataSourceContainer unmarshal(File yamlFile) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(yamlFile);
        Throwable var2 = null;

        Object var5;
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            Throwable var4 = null;

            try {
                var5 = (DynamicDataSourceContainer)(new Yaml(new Constructor(DynamicDataSourceContainer.class))).loadAs(inputStreamReader, DynamicDataSourceContainer.class);
            } catch (Throwable var28) {
                var5 = var28;
                var4 = var28;
                throw var28;
            } finally {
                if (inputStreamReader != null) {
                    if (var4 != null) {
                        try {
                            inputStreamReader.close();
                        } catch (Throwable var27) {
                            var4.addSuppressed(var27);
                        }
                    } else {
                        inputStreamReader.close();
                    }
                }

            }
        } catch (Throwable var30) {
            var2 = var30;
            throw var30;
        } finally {
            if (fileInputStream != null) {
                if (var2 != null) {
                    try {
                        fileInputStream.close();
                    } catch (Throwable var26) {
                        var2.addSuppressed(var26);
                    }
                } else {
                    fileInputStream.close();
                }
            }

        }

        return (DynamicDataSourceContainer)var5;
    }
}