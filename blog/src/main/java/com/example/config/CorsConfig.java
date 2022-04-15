package com.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * 全局跨域处理
 *
 * @Configuration: 用于定义配置类，可替换xml配置文件
 *
 * WebMvcConfigurer配置类其实是Spring内部的一种配置方式，采用JavaBean的形式来代替传统的xml配置文件形式
 *      进行针对框架个性化定制，可以自定义一些Handler，Interceptor，ViewResolver，MessageConverter
 *
 * addCorsMappings：跨域  使用此方法配置之后再使用自定义拦截器时跨域相关配置就会失效
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true)
                .maxAge(3600)
                .allowedHeaders("*");
    }

}
