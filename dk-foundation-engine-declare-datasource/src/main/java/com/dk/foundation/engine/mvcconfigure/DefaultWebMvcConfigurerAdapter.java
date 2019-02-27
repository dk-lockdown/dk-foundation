package com.dk.foundation.engine.mvcconfigure;

import com.dk.foundation.engine.intercepter.IpInterceptor;
import com.dk.foundation.engine.intercepter.CrossOriginInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * Created by duguk on 2018/1/5.
 */
public class DefaultWebMvcConfigurerAdapter extends WebMvcConfigurationSupport {
    @Value("${system.secure.open:false}")
    private String secureOpen;

    @Value("${system.secure.ip_allow:*}")
    private String secureIpAllow;

    public void addExtInterceptors(InterceptorRegistry registry) {

    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CrossOriginInterceptor()).addPathPatterns("/**");
        // ip拦截器
        if ("true".equals(secureOpen.trim().toLowerCase())) {
            registry.addInterceptor(new IpInterceptor(secureIpAllow)).addPathPatterns("/**");
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

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }
}
