package com.dk.foundation.engine;

import com.dk.foundation.common.IPUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by duguk on 2018/1/5.
 */
public class IpInterceptor implements HandlerInterceptor {
    final static Logger logger = LoggerFactory.getLogger(IpInterceptor.class);

    private String patterns;

    public IpInterceptor(String patterns){
        this.patterns = patterns;
    }


    @Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
            throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.debug("-------check ip--------");
        //获取用户ip，放在用户的request中，这里需要考虑nginx做反向代理的情况
        String ip = IPUtils.getIpAddr(request);

        if(!validation(ip)){
            logger.warn("secure->remote ip:"+ip+" is not allowed. allowed scope["+patterns+"]");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return false;
        }
        request.setAttribute("clientIp", ip);
        return true;
    }

    /**
     * ip验证
     * @param remoteip
     * @return
     */
    public boolean validation(String remoteip){
        if(patterns==null||patterns.trim().equals("")){
            return true;
        }
        String[] ips = patterns.trim().replaceAll("，", ",").split(",");
        for(String ip:ips){
            if(ip.trim().equals(remoteip.trim())){
                return true;
            }else if(ip.trim().equals("*")){
                return true;
            }
        }
        return false;
    }
}
