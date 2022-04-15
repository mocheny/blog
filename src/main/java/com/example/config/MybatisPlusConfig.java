package com.example.config;


import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * MyBatis-plus类
 *
 *Spring Boot 使用事务非常简单，首先使用注解 @EnableTransactionManagement 开启事务支持后，
 *              然后在访问数据库的Service方法上添加注解 @Transactional
 *
 * @mapperScan 注解指定要变成实现类的接口所在的包，然后包下面的所有接口在编译之后都会生成相应的实现类
 *
 * PaginationInterceptor是一个分页插件
 *
 */

@Configuration
@EnableTransactionManagement
@MapperScan("com.example.mapper")
public class MybatisPlusConfig {

    /**
     * 凡是子类及带属性、方法的类都注册Bean到Spring中，交给它管理；
     *
     * @Bean 用在方法上，告诉Spring容器，你可以从下面这个方法中拿到一个Bean
     *
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        return paginationInterceptor;
    }
}
