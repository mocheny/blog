package com.example.config;


import com.example.shiro.AccountRealm;

import com.example.shiro.JwtFilter;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * shiro配置类
 *
 *考虑到后面可能需要做集群、负载均衡等，所以就需要会话共享，而shiro的缓存和会话信息，我们一般考虑使用redis来存储这些数据，
 * 所以，我们不仅仅需要整合shiro，同时也需要整合redis。
 * 在开源的项目中，找到了一个starter可以快速整合shiro-redis，配置简单
 *
 * 因为需要做的是前后端分离项目的骨架，所以一般会采用token或者jwt作为跨域身份验证解决方案。
 * 所以整合shiro的过程中，需要引入jwt的身份验证过程。
 *
 */

@Configuration
public class ShiroConfig {

    @Autowired
    JwtFilter jwtFilter;

    /**
     *SessionManager用于管理Shiro中的Session信息
     *
     * 引入RedisSessionDAO和RedisCacheManager，为了解决shiro的权限数据和会话信息能保存到redis中，
     *      实现会话共享
     *
     */
    @Bean
    public SessionManager sessionManager(RedisSessionDAO redisSessionDAO) {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionDAO(redisSessionDAO);
        return sessionManager;
    }

    /**
     *为了关闭shiro自带的session方式，我们需要设置为false，
     *      这样用户就不再能通过session方式登录shiro。后面将采用jwt凭证登录。
     *
     */
    @Bean
    public DefaultWebSecurityManager securityManager(AccountRealm accountRealm,
                                                     SessionManager sessionManager,
                                                     RedisCacheManager redisCacheManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager(accountRealm);
        securityManager.setSessionManager(sessionManager);
        securityManager.setCacheManager(redisCacheManager);
        /*
         * 关闭shiro自带的session，详情见文档
         */
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);
        return securityManager;
    }

    /**
     * 不再通过编码形式拦截Controller访问路径，而是所有的路由都需要经过JwtFilter这个过滤器，
     * 然后判断请求头中是否含有jwt的信息，有就登录，没有就跳过。
     * 跳过之后，有Controller中的shiro注解进行再次拦截，比如@RequiresAuthentication，这样控制权限访问。
     *
     */
    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        Map<String, String> filterMap = new LinkedHashMap<>();
        filterMap.put("/**", "jwt"); // 主要通过注解方式校验权限
        chainDefinition.addPathDefinitions(filterMap);
        return chainDefinition;
    }


    @Bean("shiroFilterFactoryBean")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager,
                                                         ShiroFilterChainDefinition shiroFilterChainDefinition) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);
        Map<String, Filter> filters = new HashMap<>();
        filters.put("jwt", jwtFilter);
        shiroFilter.setFilters(filters);
        Map<String, String> filterMap = shiroFilterChainDefinition.getFilterChainMap();
        shiroFilter.setFilterChainDefinitionMap(filterMap);
        return shiroFilter;
    }

    // 开启注解代理（默认好像已经开启，可以不要）
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    @Bean
    public static DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        return creator;
    }

}
