package com.dk.foundation.engine.apollo;

import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.stereotype.Component;

@Component
public class ApolloRefreshConfig {
    private static final Logger logger = LoggerFactory.getLogger(ApolloRefreshConfig.class);

    @Autowired
    private RefreshScope refreshScope;

    @ApolloConfigChangeListener

    private void onChange(ConfigChangeEvent changeEvent) {
        refreshScope.refreshAll();
    }
}
