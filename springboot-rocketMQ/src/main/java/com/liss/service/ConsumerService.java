package com.liss.service;

import com.liss.conf.MessageEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

//@Component
public class ConsumerService //implements ApplicationListener<MessageEvent> {
{

    //@Override
    public void onApplicationEvent(MessageEvent messageEvent) {
        messageEvent.getMsgs().forEach(messageExt -> {
            System.out.printf(String.valueOf(messageExt.getBody()));
        });
    }
}
