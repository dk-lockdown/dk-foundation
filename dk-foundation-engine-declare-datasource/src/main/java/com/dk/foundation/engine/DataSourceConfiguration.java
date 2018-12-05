package com.dk.foundation.engine;

import com.google.common.base.Preconditions;
import com.dk.foundation.common.PropertyUtil;
import com.dk.foundation.engine.dynamicdatasource.DataSourceUtil;
import com.dk.foundation.engine.dynamicdatasource.DynamicDataSourceConfiguration;
import com.sun.jdi.ClassNotPreparedException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import javax.sql.DataSource;
import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by duguk on 2018/1/8.
 */
@Configuration
@EnableConfigurationProperties(DynamicDataSourceConfiguration.class)
@RefreshScope
@RequiredArgsConstructor
public class DataSourceConfiguration implements EnvironmentAware {
    final static Logger logger = LoggerFactory.getLogger(DataSourceConfiguration.class);

    private final DynamicDataSourceConfiguration propMapProperties;

    private final Map<String, DataSource> dataSourceMap = new LinkedHashMap<>();

    @Bean(name = "myBatisDataSource")
    @Primary
    public DynamicDataSource dataSource() throws IOException {
        logger.info("-------------------- dynamicDataSource init ---------------------");
//        DynamicDataSourceContainer dynamicDataSourceContainer = unmarshal(new File(
//                DynamicDataSourceContainer.class.getResource("/META-INF/muiltiDataSource.yaml").getFile()));
        DynamicDataSourceContainer dynamicDataSourceContainer = new DynamicDataSourceContainer();
        dynamicDataSourceContainer.setDataSources(dataSourceMap);
        return new DynamicDataSource(dynamicDataSourceContainer.getDataSources().get("master"), dynamicDataSourceContainer.getDataSources());
    }

    @Override
    public final void setEnvironment(final Environment environment) {
        setDataSourceMap(environment);
    }

    @SuppressWarnings("unchecked")
    private void setDataSourceMap(final Environment environment) {
        String prefix = "dynamic.datasource.";
        String dataSources = environment.getProperty(prefix + "names");
        for (String each : dataSources.split(",")) {
            try {
                Map<String, Object> dataSourceProps = PropertyUtil.handle(environment, prefix + each.trim(), Map.class);
                Preconditions.checkState(!dataSourceProps.isEmpty(), "Wrong datasource properties!");
                DataSource dataSource = DataSourceUtil.getDataSource(dataSourceProps.get("type").toString(), dataSourceProps);
                dataSourceMap.put(each, dataSource);
            } catch (final ReflectiveOperationException ex) {
                throw new ClassNotPreparedException("Can't find datasource type!");
            }
        }
    }

//    private DynamicDataSourceContainer unmarshal(File yamlFile) throws IOException {
//        FileInputStream fileInputStream = new FileInputStream(yamlFile);
//        Throwable var2 = null;
//
//        Object var5;
//        try {
//            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
//            Throwable var4 = null;
//
//            try {
//                var5 = (DynamicDataSourceContainer)(new Yaml(new Constructor(DynamicDataSourceContainer.class))).loadAs(inputStreamReader, DynamicDataSourceContainer.class);
//            } catch (Throwable var28) {
//                var5 = var28;
//                var4 = var28;
//                throw var28;
//            } finally {
//                if (inputStreamReader != null) {
//                    if (var4 != null) {
//                        try {
//                            inputStreamReader.close();
//                        } catch (Throwable var27) {
//                            var4.addSuppressed(var27);
//                        }
//                    } else {
//                        inputStreamReader.close();
//                    }
//                }
//
//            }
//        } catch (Throwable var30) {
//            var2 = var30;
//            throw var30;
//        } finally {
//            if (fileInputStream != null) {
//                if (var2 != null) {
//                    try {
//                        fileInputStream.close();
//                    } catch (Throwable var26) {
//                        var2.addSuppressed(var26);
//                    }
//                } else {
//                    fileInputStream.close();
//                }
//            }
//
//        }
//
//        return (DynamicDataSourceContainer)var5;
//    }
}