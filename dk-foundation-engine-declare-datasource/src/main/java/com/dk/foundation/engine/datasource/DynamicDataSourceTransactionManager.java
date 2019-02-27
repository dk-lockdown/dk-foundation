package com.dk.foundation.engine.datasource;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;

import javax.sql.DataSource;

public class DynamicDataSourceTransactionManager extends DataSourceTransactionManager {
    public DynamicDataSourceTransactionManager(DataSource dataSource)
    {
        super(dataSource);
    }

    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) {
        if (definition.isReadOnly()) {
            DynamicDataSourceKey.setDataSource(DynamicDataSourceKey.READ_RANDOM_PROXY);
        } else {
            DynamicDataSourceKey.setDataSource(DynamicDataSourceKey.MASTER);
        }
        super.doBegin(transaction, definition);
    }

    @Override
    protected void doCleanupAfterCompletion(Object transaction) {
        super.doCleanupAfterCompletion(transaction);
        DynamicDataSourceKey.clearDataSource();
    }
}
