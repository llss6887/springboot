package com.liss.filterMes;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.util.List;
import java.util.stream.Collectors;

public class RocketMQFilterBySql {
    public static void main(String[] args) throws Exception {
        filterProducer();
        filterConsumer();
    }

    private static void filterConsumer() throws Exception{
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("tag_consumer");
        consumer.setNamesrvAddr("192.168.25.7:9876");
        //两种方式都可以
        consumer.subscribe("topic_sql", MessageSelector.bySql("filter = 1"));
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                list.stream().filter(item -> item.getUserProperty("filter") == "1").collect(Collectors.toList());
                list.forEach(messageExt -> {
                    System.out.println(new String(messageExt.getBody()));
                });
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
    }

    private static void filterProducer() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("tag_producer");
        producer.setNamesrvAddr("192.168.25.7:9876");
        producer.start();
        Message message = new Message("topic_sql","tag", ("this is filter message:").getBytes(RemotingHelper.DEFAULT_CHARSET));
        message.putUserProperty("filter", "2");
        producer.send(message);
        producer.shutdown();
    }
}
