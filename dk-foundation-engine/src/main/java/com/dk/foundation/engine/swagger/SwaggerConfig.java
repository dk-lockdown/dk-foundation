package com.dk.foundation.engine.swagger;

import com.google.common.base.Predicates;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by duguk on 2018/1/5.
 */
@Configuration
@EnableSwagger2
@RefreshScope
public class SwaggerConfig {
    private static final String HOST_NONE = "none";

    @Value("${spring.application.name}")
    String title;

    @Value("${spring.application.swagger.host:none}")
    String host;

    @Value("${spring.application.swagger.basePackage:com.dk}")
    String basePackage;

    @Value("${spring.application.swagger.enable:true}")
    Boolean enable;

    @Bean
    @RefreshScope
    public Docket customDocket() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage(basePackage))
                .paths(Predicates.not(PathSelectors.regex("/error.*")))
                .build()
                .apiInfo(apiInfo())
                .enable(enable);
        if(!host.equals(HOST_NONE)){
            docket.host(host);
        }
        return docket;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(title)
                .description("")
                .version("1.0.0")
                .build();
    }
}
