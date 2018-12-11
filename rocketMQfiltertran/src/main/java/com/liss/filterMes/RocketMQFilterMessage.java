package com.liss.filterMes;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.util.List;

public class RocketMQFilterMessage {
    public static void main(String[] args) throws Exception {
        filterProducer();
        filterConsumer();
    }

    private static void filterConsumer() throws Exception{
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("tag_consumer");
        consumer.setNamesrvAddr("192.168.25.7:9876");
        //两种方式都可以
        consumer.subscribe("topic", MessageSelector.byTag("tag_1 || tag_2"));
        //consumer.subscribe("topic", "tag_1 || tag_2 || tag_3");
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
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
        for (int i = 0; i < 10; i++) {
            Message message = new Message("topic","tag_"+i, ("this is filter message:"+i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            producer.send(message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    System.out.println("消息发送状态:"+sendResult.getSendStatus());
                }

                @Override
                public void onException(Throwable throwable) {
                    throwable.printStackTrace();
                }
            });
        }
    }
}
