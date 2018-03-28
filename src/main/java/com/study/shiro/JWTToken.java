package com.study.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * Created by user on 2018/3/22.
 */
public class JWTToken implements AuthenticationToken{
    // 密钥
    private String token;

    public JWTToken(String token) {
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
