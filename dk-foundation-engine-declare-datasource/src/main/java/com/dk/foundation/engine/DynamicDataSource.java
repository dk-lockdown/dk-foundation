package com.dk.foundation.engine;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class DynamicDataSource extends AbstractRoutingDataSource {
    private List<String> dataSourceKeys;

    public DynamicDataSource(DataSource defaultTargetDataSource, Map<String, DataSource> targetDataSources) {
        super.setDefaultTargetDataSource(defaultTargetDataSource);
        super.setTargetDataSources(new HashMap<>(targetDataSources));
        super.afterPropertiesSet();
        dataSourceKeys=targetDataSources.keySet().stream().collect(Collectors.toList());
    }

    @Override
    protected Object determineCurrentLookupKey() {
        String dataSourceKey = DynamicDataSourceKey.getDataSourceKey();
        if(StringUtils.isBlank(dataSourceKey))
        {
            return DynamicDataSourceKey.MASTER;
        }
        if (dataSourceKey.equals(DynamicDataSourceKey.READ_RANDOM_PROXY)) {
            if (dataSourceKeys != null && dataSourceKeys.size() > 0) {
                List<String> readDataSourceKeys = dataSourceKeys.stream().filter(s->!s.equals(DynamicDataSourceKey.MASTER)).collect(Collectors.toList());
                if(readDataSourceKeys!=null&&readDataSourceKeys.size()>0) {
                    Random random = new Random();
                    int index = random.nextInt(readDataSourceKeys.size());
                    return readDataSourceKeys.get(index);
                }
            }
        }
        return dataSourceKey;
    }
}
