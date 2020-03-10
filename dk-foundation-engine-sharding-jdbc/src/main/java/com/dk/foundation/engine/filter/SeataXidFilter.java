package com.dk.foundation.engine.filter;

import com.dk.foundation.common.SeataConstants;
import io.seata.core.context.RootContext;
import io.shardingsphere.api.HintManager;
import io.shardingsphere.core.hint.HintManagerHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SeataXidFilter extends OncePerRequestFilter {
    protected Logger logger = LoggerFactory.getLogger(SeataXidFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String xid = RootContext.getXID();
        String restXid = request.getHeader(SeataConstants.XID_HEADER);
        boolean bind = false;
        if(StringUtils.isBlank(xid)&&StringUtils.isNotBlank(restXid)){
            RootContext.bind(restXid);
            bind = true;

            logger.info("bind[" + restXid + "] to RootContext");
        }
        try{
            if(RootContext.inGlobalTransaction()){
                HintManager hintManager = HintManagerHolder.get();
                if(hintManager==null){
                    hintManager = HintManager.getInstance();
                }
                hintManager.setMasterRouteOnly();
            }
            filterChain.doFilter(request, response);
            if(RootContext.inGlobalTransaction()){
                HintManager hintManager = HintManagerHolder.get();
                if(hintManager!=null) {
                    hintManager.close();
                }
            }
        } finally {
            if (bind) {
                String unbindXid = RootContext.unbind();
                logger.info("unbind[" + unbindXid + "] from RootContext");
                if (!restXid.equalsIgnoreCase(unbindXid)) {
                    logger.warn("xid in change during http rest from " + restXid + " to " + unbindXid);
                    if (unbindXid != null) {
                        RootContext.bind(unbindXid);
                        logger.warn("bind [" + unbindXid + "] back to RootContext");
                    }
                }
            }
        }
    }
}
