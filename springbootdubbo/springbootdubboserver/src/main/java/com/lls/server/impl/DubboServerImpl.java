package com.lls.server.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.lls.server.DubboServer;
import org.springframework.stereotype.Component;

@Component("dubboServer")
@Service(version = "1.0.0", interfaceClass = DubboServer.class)
public class DubboServerImpl implements DubboServer {

    @Override
    public String getName(String name){
        return "Dubbo " + name;
    }
}
