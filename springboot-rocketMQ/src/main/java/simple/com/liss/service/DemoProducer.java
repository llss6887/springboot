package simple.com.liss.service;


import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import simple.com.liss.domain.DemoEvent;

import javax.annotation.PostConstruct;

@PropertySource(value = "classpath:rocketMQ.properties")
@Component
@Slf4j
public class DemoProducer {

    @Value("${com.liss.producer.group}")
    private String producer_group;

    @Value("${com.liss.namesrvAddr}")
    private String namesrvAddr;

    @Value("${com.liss.topic}")
    private String topic;

    @Value("${com.liss.tag}")
    private String tag;


    //对象初始化就执行 相当于servlet的init
    @PostConstruct
    public void demoProducer(){
        DefaultMQProducer producer = new DefaultMQProducer(producer_group);
        producer.setNamesrvAddr(namesrvAddr);
        producer.setRetryTimesWhenSendFailed(3);
        try{
            producer.start();
            for (int i = 0; i < 50; i++) {
                DemoEvent event = new DemoEvent("lisi"+i, "这是一个message"+i);
                Message message = new Message(topic, tag, event.getName(), event.getMessage().getBytes("UTF-8"));
                SendResult sendResult = producer.send(message);
                System.err.println("生产者在生产："+sendResult.getMsgId());
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
        }finally {
            producer.shutdown();
        }
    }
}
