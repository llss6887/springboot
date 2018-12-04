package com.llss.consumer;


import com.llss.produce.ActiveMQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class ActiveMQConsumer {
    @Autowired
    private ActiveMQProducer activeMQProducer;

    @JmsListener(destination = "my_queue")
    public void distribute(String json){
        System.out.println(json);
    }
}
