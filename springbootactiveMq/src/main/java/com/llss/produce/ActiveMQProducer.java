package com.llss.produce;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.jms.Destination;


@Service("registerMailboxProducer")
public class ActiveMQProducer {
    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    public void send(Destination destination, String json){
        jmsMessagingTemplate.convertAndSend(destination, json);
    }
}
