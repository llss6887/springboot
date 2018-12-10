package com.llss.realm;


import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ShiroConfiguration {

    /**
     * 配置权限管理的认证和授权
     * @return
     */
    @Bean
    public MyShiroRealm getRealm(){
        MyShiroRealm realm = new MyShiroRealm();
        realm.setCredentialsMatcher(customCredentialsMatcher());
        return realm;
    }

    /**
     * 认证对比
     * @return
     */
    @Bean
    public CustomCredentialsMatcher customCredentialsMatcher(){
        return new CustomCredentialsMatcher();
    }

    /**
     * 安全管理器
     * @return
     */
    @Bean
    public DefaultSecurityManager securityManager(){
        DefaultSecurityManager dsn = new DefaultWebSecurityManager();
        dsn.setRealm(getRealm());
        return dsn;
    }

    /**
     * 过滤器
     * @param securityManager
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultSecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String, String> map = new HashMap<>();
        map.put("/logout", "logout");
        map.put("/**", "authc");
        shiroFilterFactoryBean.setLoginUrl("/login");
        shiroFilterFactoryBean.setSuccessUrl("/index");
        shiroFilterFactoryBean.setUnauthorizedUrl("/error");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        return shiroFilterFactoryBean;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultSecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor aas = new AuthorizationAttributeSourceAdvisor();
        aas.setSecurityManager(securityManager);
        return aas;
    }

}
