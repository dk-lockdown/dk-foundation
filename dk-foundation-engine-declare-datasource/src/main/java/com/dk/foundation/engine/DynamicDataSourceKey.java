package com.dk.foundation.engine;


import org.apache.commons.lang3.StringUtils;

public class DynamicDataSourceKey {
    public static final String MASTER="master";
    public static final String READ_RANDOM_PROXY="read-random-proxy";
    private static final ThreadLocal<String> annotationKey = new ThreadLocal<>();
    private static final ThreadLocal<String> pluginKey = new ThreadLocal<>();

    public static String getDataSourceKey() {
        String dataSourceKey = annotationKey.get();
        if (!StringUtils.isBlank(dataSourceKey)) {
            return dataSourceKey;
        } else {
            return pluginKey.get();
        }
    }

    public static void setDataSource(String dataSourceKey) {
        annotationKey.set(dataSourceKey);
    }

    public static void setDataSource(String dataSourceKey,boolean isAnnotationKey) {
        if (isAnnotationKey) {
            annotationKey.set(dataSourceKey);
        } else {
            pluginKey.set(dataSourceKey);
        }
    }

    public static void clearDataSource() {
        annotationKey.remove();
        pluginKey.remove();
    }
}
