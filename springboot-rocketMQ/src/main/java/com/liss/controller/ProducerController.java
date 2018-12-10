package com.liss.controller;

import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.*;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.common.message.MessageQueue;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProducerController {

    @Autowired
    @Qualifier("defaultMQProducer")
    private DefaultMQProducer defaultMQProducer;

    @Autowired
    @Qualifier("transactionMQProducer")
    private TransactionMQProducer transactionMQProducer;

    @RequestMapping("/sendMessage")
    public void sendMessage() throws Exception {
        for (int i = 0; i < 20; i++) {
            Message message = new Message("liss_topic", "first", ("message " + i).getBytes());
            SendResult send = defaultMQProducer.send(message);
            System.out.println("发送状态："+send.getSendStatus());
        }
        defaultMQProducer.shutdown();
    }

    @RequestMapping("/sendTransactionMess")
    public void sendTransactionMess() throws Exception {
        SendResult sendResult = null;
        try {
            // a,b,c三个值对应三个不同的状态
            String ms = "c";
            Message msg = new Message("liss_topic","first",ms.getBytes());
            // 发送事务消息
            sendResult = transactionMQProducer.sendMessageInTransaction(msg, (Message msg1, Object arg) -> {
                String value = "";
                if (arg instanceof String) {
                    value = (String) arg;
                }
                if (value == "") {
                    throw new RuntimeException("发送消息不能为空...");
                } else if (value =="a") {
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                } else if (value =="b") {
                    return LocalTransactionState.COMMIT_MESSAGE;
                }
                return LocalTransactionState.ROLLBACK_MESSAGE;
            }, 4);
            System.out.println("事务生生产者状态：" + sendResult.getSendStatus());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/orderSendMessage")
    public void orderSendMessage(){
        for(int i=0;i<100;i++) {
            Message message = new Message("liss_topic", "first", ("order_message"+i).getBytes());
            try {
                SendResult sendResult = defaultMQProducer.send(message, new MessageQueueSelector() {
                    @Override
                    public MessageQueue select(List<MessageQueue> list, Message message, Object o) {
                        int index = ((Integer) o) % list.size();
                        return list.get(index);
                    }
                }, i);
                System.out.println("生产的消息："+sendResult.getSendStatus());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
