package com.dk.foundation.engine.datasource.dynamicdatasource;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

@ConfigurationProperties(prefix = "dynamic.datasource")
public class DynamicDataSourceConfiguration {
    private String defaultDataSourceName;
    private Map<String, Object> configMap = new LinkedHashMap();
    private Properties props = new Properties();

    public DynamicDataSourceConfiguration(Map<String, Object> configMap, Properties props) {
        this.configMap = configMap;
        this.props = props;
    }

    public DynamicDataSourceConfiguration() {
    }

    public String getDefaultDataSourceName() {
        return this.defaultDataSourceName;
    }

    public Map<String, Object> getConfigMap() {
        return this.configMap;
    }

    public Properties getProps() {
        return this.props;
    }

    public void setDefaultDataSourceName(String defaultDataSourceName) {
        this.defaultDataSourceName = defaultDataSourceName;
    }

    public void setConfigMap(Map<String, Object> configMap) {
        this.configMap = configMap;
    }

    public void setProps(Properties props) {
        this.props = props;
    }
}
