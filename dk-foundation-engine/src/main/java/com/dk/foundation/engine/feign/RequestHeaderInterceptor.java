package com.dk.foundation.engine.feign;

import com.dk.foundation.common.SeataConstants;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.seata.core.context.RootContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.collections.CaseInsensitiveKeyMap;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by duguk on 2018/1/5.
 */
@Component
public class RequestHeaderInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        /**
         * 获取不到springmvc请求上下文直接返回
         */
        if (attributes == null) {
            return;
        }

        boolean seataTransactionATMode = true;
        if (attributes!=null) {
            HttpServletRequest request = attributes.getRequest();
            Enumeration<String> headerNames = request.getHeaderNames();
            if (headerNames != null) {
                Map<String, Collection<String>> resolvedHeaders = new CaseInsensitiveKeyMap<>();
                resolvedHeaders.putAll(template.headers());

                while (headerNames.hasMoreElements()) {
                    String name = headerNames.nextElement();
                    if (!resolvedHeaders.containsKey(name)) {
                        String values = request.getHeader(name);
                        List<String> headers = new ArrayList<String>();
                        headers.addAll(Arrays.asList(values));
                        resolvedHeaders.put(name, headers);
                    }
                }
                template.headers(null);
                template.headers(resolvedHeaders);
            }
        }
        Map<String, Collection<String>> headers = template.headers();
        if(headers!=null){
            Collection<String> values = headers.getOrDefault(SeataConstants.TRANSACTION_MODE_HEADER,null);
            if (values==null) {
                values = headers.getOrDefault(SeataConstants.TRANSACTION_MODE_HEADER.toLowerCase(),null);
            }
            if(values!=null&&values.contains("TCC")){
                seataTransactionATMode = false;
            }
        }
        if(seataTransactionATMode) {
            String xid = RootContext.getXID();
            if (StringUtils.isNotBlank(xid)) {
                template.header(SeataConstants.XID_HEADER, xid);
            }
        }
    }
}
