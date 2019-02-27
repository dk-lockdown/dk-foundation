package com.dk.foundation.engine.datasource;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {
                MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {
                MappedStatement.class, Object.class, RowBounds.class,
                ResultHandler.class
        })
})
public class DynamicDataSourcePlugin implements Interceptor {
    protected static final Logger logger = LoggerFactory.getLogger(DynamicDataSourcePlugin.class);

    private static final String REGEX = ".*insert\\s+into.*|.*delete\\s+from\\s+.*|.*update\\s+.*set\\s+.*";
    private static final Pattern PATTERN = Pattern.compile(REGEX, Pattern.CASE_INSENSITIVE);
    private static final Map<String, String> CACHE_MAP = new ConcurrentHashMap<>();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        boolean synchronizationActive = TransactionSynchronizationManager.isSynchronizationActive();
        if(!synchronizationActive) {
            Object[] objects = invocation.getArgs();
            MappedStatement ms = (MappedStatement) objects[0];
            String dataSourceKey="";
            if((dataSourceKey = CACHE_MAP.get(ms.getId())) == null) {
                if(ms.getSqlCommandType().equals(SqlCommandType.SELECT)) {
                    //!selectKey 为自增id查询主键(SELECT LAST_INSERT_ID() )方法，使用主库
                    if(ms.getId().contains(SelectKeyGenerator.SELECT_KEY_SUFFIX)) {
                        dataSourceKey = DynamicDataSourceKey.MASTER;
                    } else {
                        BoundSql boundSql = ms.getSqlSource().getBoundSql(objects[1]);
                        String sql = boundSql.getSql().replaceAll("[\\t\\n\\r]", " ");
                        if(PATTERN.matcher(sql).matches()) {
                            dataSourceKey = DynamicDataSourceKey.MASTER;
                        } else {
                            dataSourceKey = DynamicDataSourceKey.READ_RANDOM_PROXY;
                        }
                    }
                }else{
                    dataSourceKey = DynamicDataSourceKey.MASTER;
                }
                logger.warn("Method [{}] use [{}] Strategy, SqlCommandType [{}].", ms.getId(), dataSourceKey, ms.getSqlCommandType().name());
                CACHE_MAP.put(ms.getId(), dataSourceKey);
            }
            DynamicDataSourceKey.setDataSource(dataSourceKey,false);
        }

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
