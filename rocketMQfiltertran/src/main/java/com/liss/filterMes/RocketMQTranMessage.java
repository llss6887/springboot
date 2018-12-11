package com.liss.filterMes;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.concurrent.ConcurrentHashMap;

public class RocketMQTranMessage {

    private static ConcurrentHashMap<String, Integer> localTrans = new ConcurrentHashMap<>();
    public static void main(String[] args) throws MQClientException {
        tranProducer();
    }

    private static void tranProducer() throws MQClientException {
        TransactionMQProducer producer = new TransactionMQProducer("tran_group");
        producer.setNamesrvAddr("192.168.25.7:9876");
        producer.setTransactionListener(new TransactionListener() {
            //执行本地事务
            @Override
            public LocalTransactionState executeLocalTransaction(Message message, Object o) {
                //将message写入消息表或者其他存储中，并保证有唯一标示
                localTrans.put(message.getTransactionId(),"唯一标示");
                return LocalTransactionState.UNKNOW;
            }
            //事务回查 三种状态 UNKNOW COMMIT_MESSAGE ROLLBACK_MESSAGE
            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
                Integer status = localTrans.get(messageExt.getTransactionId());
                //根据本地事务执行的状态，决定是发给消费者还是不通知消费者，或者回滚
                switch (status){
                    case 0:
                        return LocalTransactionState.UNKNOW;
                    case 1:
                        return LocalTransactionState.COMMIT_MESSAGE;
                    case 2:
                        return LocalTransactionState.ROLLBACK_MESSAGE;
                }
                return LocalTransactionState.COMMIT_MESSAGE;
            }
        });
        producer.start();
        producer.sendMessageInTransaction(new Message("tran_topic","tran","ceshi".getBytes()), null);

    }

}
