package com.example.shiro;


import com.example.entity.User;
import com.example.service.UserService;

import com.example.util.JwtUtils;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.apachecommons.CommonsLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @component （把普通pojo实例化到spring容器中，相当于配置文件中的 <bean id="" class=""/>）
 *
 *
 *主要就是doGetAuthenticationInfo登录认证这个方法，可以看到我们通过jwt获取到用户信息，
 * 判断用户的状态，最后异常就抛出对应的异常信息，否者封装成SimpleAuthenticationInfo返回给shiro。
 *
 *
 *
 */
@Slf4j
@Component
public class AccountRealm extends AuthorizingRealm {
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserService userService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        JwtToken jwt = (JwtToken) token;
        log.info("jwt----------------->{}", jwt);
        String userId = jwtUtils.getClaimByToken((String) jwt.getPrincipal()).getSubject();
        User user = userService.getById(Long.parseLong(userId));
        if(user == null) {
            throw new UnknownAccountException("账户不存在！");
        }
        if(user.getStatus() == -1) {
            throw new LockedAccountException("账户已被锁定！");
        }
        AccountProfile profile = new AccountProfile();
        BeanUtil.copyProperties(user, profile);
        log.info("profile----------------->{}", profile.toString());
        return new SimpleAuthenticationInfo(profile, jwt.getCredentials(), getName());
    }
}
