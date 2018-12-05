package com.llss.realm;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

/**
 * 加密后对比是否一致
 */
public class CustomCredentialsMatcher extends SimpleCredentialsMatcher {
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        Object tokentials = Encrypt.md5(String.valueOf(usernamePasswordToken.getPassword()), usernamePasswordToken.getUsername());
        Object credentials = getCredentials(info);
        return equals(tokentials, credentials);
    }
}
