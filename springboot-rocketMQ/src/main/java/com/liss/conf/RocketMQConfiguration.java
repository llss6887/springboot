package com.liss.conf;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.*;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.LocalTransactionState;
import com.alibaba.rocketmq.client.producer.TransactionCheckListener;
import com.alibaba.rocketmq.client.producer.TransactionMQProducer;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.alibaba.rocketmq.common.protocol.heartbeat.MessageModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;

@Configuration
//加载配置文件到RocketPro中
@EnableConfigurationProperties(RocketPro.class)
@Slf4j
public class RocketMQConfiguration {

    @Autowired
    private RocketPro rocketPro;

    @Autowired
    private ApplicationEventPublisher publisher = null;

    /**
     * 初始化打印配置信息
     */
    @PostConstruct
    public void init(){
        System.err.println(rocketPro.getNamesrvAddr());
        System.err.println(rocketPro.getProducerGroupName());
        System.err.println(rocketPro.getConsumerBatchMaxSize());
        System.err.println(rocketPro.getConsumerGroupName());
        System.err.println(rocketPro.getConsumerInstanceName());
        System.err.println(rocketPro.getProducerInstanceName());
        System.err.println(rocketPro.getProducerTranInstanceName());
        System.err.println(rocketPro.getTransactionProducerGroupName());
        System.err.println(rocketPro.isConsumerBroadcasting());
        System.err.println(rocketPro.isEnableHistoryConsumer());
        System.err.println(rocketPro.isEnableOrderConsumer());
        System.out.println(rocketPro.getSubscribe().get(0));
    }

    /**
     * 生产者
     * @return
     * @throws MQClientException
     */
    @Bean
    @Qualifier("defaultMQProducer")
    public DefaultMQProducer defaultMQProducer() throws MQClientException {
        DefaultMQProducer producer = new DefaultMQProducer(rocketPro.getProducerGroupName());
        producer.setNamesrvAddr(rocketPro.getNamesrvAddr());
        producer.setRetryTimesWhenSendFailed(5);
        producer.setInstanceName(rocketPro.getProducerInstanceName());
        producer.start();
        log.info("生产者启动");
        return producer;
    }

    /**
     * 事务生产者
     * @return
     * @throws MQClientException
     */
    @Bean
    @Qualifier("transactionMQProducer")
    public TransactionMQProducer transactionMQProducer() throws MQClientException {
        TransactionMQProducer producer = new TransactionMQProducer(rocketPro.getTransactionProducerGroupName());
        producer.setNamesrvAddr(rocketPro.getNamesrvAddr());
        producer.setRetryTimesWhenSendFailed(5);
        producer.setInstanceName(rocketPro.getProducerTranInstanceName());
        producer.setCheckThreadPoolMinSize(2);
        producer.setCheckThreadPoolMaxSize(10);
        producer.setCheckRequestHoldMax(100);
        //3.2.6版本已经没有了，可以外部通过定时扫描解决, 当为COMMIT_MESSAGE 是消费者才能收到消息
        producer.setTransactionCheckListener(new TransactionCheckListener() {
            @Override
            public LocalTransactionState checkLocalTransactionState(MessageExt messageExt) {
                System.out.println(messageExt.getMsgId());
                return LocalTransactionState.COMMIT_MESSAGE;
            }
        });
        producer.start();
        log.info("事务生产者启动");
        return producer;
    }

    /**
     * 消费者
     * @return
     * @throws Exception
     */
    @Bean
    public DefaultMQPushConsumer defaultMQPushConsumer() throws Exception{
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(rocketPro.getConsumerGroupName());
        consumer.setNamesrvAddr(rocketPro.getNamesrvAddr());
        consumer.setConsumerGroup(rocketPro.getConsumerGroupName());
        consumer.setInstanceName(rocketPro.getConsumerInstanceName());
        consumer.setPullBatchSize(rocketPro.getConsumerBatchMaxSize());
        //判断是否是广播消息
        if(rocketPro.isConsumerBroadcasting()){
            consumer.setMessageModel(MessageModel.BROADCASTING);
        }
        //设置批量
        consumer.setConsumeMessageBatchMaxSize(rocketPro.getConsumerBatchMaxSize() == 0?1:rocketPro.getConsumerBatchMaxSize());
        rocketPro.getSubscribe().forEach(str->{
            String[] topic_tag = str.split(":");
            try {
                consumer.subscribe(topic_tag[0], topic_tag[1]);
            } catch (MQClientException e) {
                e.printStackTrace();
                log.error(e.getMessage());
            }
        });
        //顺序消费
        if(rocketPro.isEnableOrderConsumer()){
            consumer.registerMessageListener(new MessageListenerOrderly() {
                @Override
                public ConsumeOrderlyStatus consumeMessage(List<MessageExt> list, ConsumeOrderlyContext consumeOrderlyContext) {
                    consumeOrderlyContext.setAutoCommit(true);
                    try {
                        publisher.publishEvent(new MessageEvent(list, consumer));
                    } catch (Exception e) {
                        e.printStackTrace();
                        return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
                    }
                    return ConsumeOrderlyStatus.SUCCESS;
                }
            });
        }else{
            consumer.registerMessageListener(new MessageListenerConcurrently() {
                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                    try {
                        //用spring的监听，将消息放到监听中
                        publisher.publishEvent(new MessageEvent(list, consumer));
                    } catch (Exception e) {
                        e.printStackTrace();
                        return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                    }
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            });
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    consumer.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return consumer;
    }
}
