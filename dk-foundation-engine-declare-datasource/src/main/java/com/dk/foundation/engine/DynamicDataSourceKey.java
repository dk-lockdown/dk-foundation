package com.dk.foundation.engine;


public class DynamicDataSourceKey {
    public static final String MASTER="master";
    public static final String READ_RANDOM_PROXY="read-random-proxy";
    private static final ThreadLocal<String> key = new ThreadLocal<>();

    public static String getDataSourceKey(){
        return key.get();
    }

    public static void setDataSource(String dataSourceKey) {
        key.set(dataSourceKey);
    }

    public static void clearDataSource() {
        key.remove();
    }
}
