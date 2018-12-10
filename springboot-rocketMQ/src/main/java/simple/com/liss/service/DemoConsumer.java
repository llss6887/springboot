package simple.com.liss.service;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.message.MessageExt;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@PropertySource(value = "classpath:rocketMQ.properties")
@Component
@Slf4j
public class DemoConsumer {
    @Value("${com.liss.consumer.group}")
    private String consumer_group;

    @Value("${com.liss.namesrvAddr}")
    private String namesrvAddr;

    @Value("${com.liss.topic}")
    private String topic;

    @Value("${com.liss.tag}")
    private String tag;

    @PostConstruct
    public void demoConsumer(){
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumer_group);
        consumer.setNamesrvAddr(namesrvAddr);
        try{
            consumer.subscribe(topic, tag);
            consumer.registerMessageListener(new MessageListenerConcurrently() {
                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {

                    try {
                        list.forEach(messageExt -> {
                            System.err.println("消费者消息：" + new String(messageExt.getBody()));
                        });
                    }catch (Exception e){
                        log.error(e.getMessage());
                        return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                    }
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            });
            consumer.start();
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }
}
