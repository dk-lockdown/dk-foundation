package com.dk.foundation.engine.datasource;


import org.apache.commons.lang3.StringUtils;

public class DynamicDataSourceKey {
    public static final String MASTER="master";
    public static final String READ_RANDOM_PROXY="read-random-proxy";
    private static final ThreadLocal<String> ANNOTATION_KEY = new ThreadLocal<>();
    private static final ThreadLocal<String> PLUGIN_KEY = new ThreadLocal<>();

    public static String getDataSourceKey() {
        String dataSourceKey = ANNOTATION_KEY.get();
        if (!StringUtils.isBlank(dataSourceKey)) {
            return dataSourceKey;
        } else {
            return PLUGIN_KEY.get();
        }
    }

    public static void setDataSource(String dataSourceKey) {
        ANNOTATION_KEY.set(dataSourceKey);
    }

    public static void setDataSource(String dataSourceKey,boolean isAnnotationKey) {
        if (isAnnotationKey) {
            ANNOTATION_KEY.set(dataSourceKey);
        } else {
            PLUGIN_KEY.set(dataSourceKey);
        }
    }

    public static void clearDataSource() {
        ANNOTATION_KEY.remove();
        PLUGIN_KEY.remove();
    }
}
