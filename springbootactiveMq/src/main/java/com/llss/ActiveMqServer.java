package com.llss;


import com.fasterxml.jackson.databind.util.JSONPObject;
import com.llss.produce.ActiveMQProducer;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.Destination;

@SpringBootApplication
@RestController
public class ActiveMqServer {

    @Value("${messages.queue}")
    private String queue;

    @Autowired
    private ActiveMQProducer activeMQProducer;

    public static void main(String[] args) {
        SpringApplication.run(ActiveMqServer.class, args);
    }


    @GetMapping("/msg/{str}")
    public void set_queue(@PathVariable String str){
        Destination amq = new ActiveMQQueue(queue);
        activeMQProducer.send(amq, str);
    }
}
