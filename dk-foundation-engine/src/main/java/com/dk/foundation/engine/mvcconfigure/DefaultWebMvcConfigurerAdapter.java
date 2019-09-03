package com.dk.foundation.engine.mvcconfigure;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.dk.foundation.engine.intercepter.CrossOriginInterceptor;
import com.dk.foundation.engine.intercepter.IpInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by duguk on 2018/1/5.
 */
@Component
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
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter fastConvert = new FastJsonHttpMessageConverter();

        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm";
        fastJsonConfig.setSerializerFeatures(SerializerFeature.BrowserCompatible,
                SerializerFeature.PrettyFormat,
                SerializerFeature.WriteDateUseDateFormat,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.DisableCircularReferenceDetect);
        /**
         * 解决Long转json精度丢失的问题
         */
        SerializeConfig serializeConfig = SerializeConfig.globalInstance;
        serializeConfig.put(BigInteger.class, ToStringSerializer.instance);
        serializeConfig.put(Long.class, ToStringSerializer.instance);
        serializeConfig.put(Long.TYPE, ToStringSerializer.instance);
        fastJsonConfig.setSerializeConfig(serializeConfig);
        fastConvert.setFastJsonConfig(fastJsonConfig);
        converters.add(fastConvert);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");    }
}
