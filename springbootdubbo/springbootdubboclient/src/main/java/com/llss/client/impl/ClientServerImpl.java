package com.llss.client.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.llss.client.ClientServer;
import com.llss.server.DubboServer;
import org.springframework.stereotype.Component;

@Component
public class ClientServerImpl implements ClientServer {

    @Reference(version = "1.0.0")
    private DubboServer dubboServer;

    @Override
    public String conService(String name){
        return dubboServer.getName(name);
    }
}
