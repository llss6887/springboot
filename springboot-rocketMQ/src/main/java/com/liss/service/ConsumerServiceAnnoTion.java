package com.liss.service;

import com.liss.conf.MessageEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ConsumerServiceAnnoTion {
    @EventListener(condition = "#event.msgs[0].topic=='liss_topic' && #event.msgs[0].tags=='first'")
    public void listenerEvent(MessageEvent event){
        event.getMsgs().forEach(messageExt -> {
            System.err.println("消费者的消息：" + new String(messageExt.getBody()));
        });
    }
}
