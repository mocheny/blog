package com.example.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * shiro默认supports的是UsernamePasswordToken，而现在采用了jwt的方式，
 * 所以这里自定义一个JwtToken，来完成shiro的supports方法
 */
public class JwtToken implements AuthenticationToken {
    private String token;
    public JwtToken(String token) {
        this.token = token;
    }
    @Override
    public Object getPrincipal() {
        return token;
    }
    @Override
    public Object getCredentials() {
        return token;
    }
}
