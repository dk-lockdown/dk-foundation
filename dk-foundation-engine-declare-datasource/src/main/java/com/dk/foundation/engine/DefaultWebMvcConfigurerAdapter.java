package com.dk.foundation.engine;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by duguk on 2018/1/5.
 */
public class DefaultWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter {
    @Value("${system.secure.open:false}")
    private String secure_open;

    @Value("${system.secure.ip_allow:*}")
    private String secure_ip_allow;

    public void addExtInterceptors(InterceptorRegistry registry) {

    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CrossOriginInterceptor()).addPathPatterns("/**");
        // ip拦截器
        if (secure_open.trim().toLowerCase().equals("true")) {
            registry.addInterceptor(new IpInterceptor(secure_ip_allow)).addPathPatterns("/**");
        }
        addExtInterceptors(registry);
        super.addInterceptors(registry);
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        AntPathMatcher matcher = new AntPathMatcher();
        matcher.setCaseSensitive(false);
        configurer.setPathMatcher(matcher);
    }
}
