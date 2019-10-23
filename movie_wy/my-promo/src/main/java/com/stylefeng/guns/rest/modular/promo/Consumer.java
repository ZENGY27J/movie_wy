package com.stylefeng.guns.rest.modular.promo;

import com.alibaba.fastjson.JSON;
import com.stylefeng.guns.rest.common.persistence.dao.MtimePromoStockMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;

/**
 * @Program: movie_wy
 * @Author: ZyEthan
 * @Description: 消息消费者
 * @Date: 2019-10-19-15:29
 */
@Component
@Slf4j
public class Consumer {

    @Value("${mq.nameserver.address}")
    private String address;

    @Value("${mq.consumergroup}")
    private String consumerGroup;

    @Value("${mq.topic}")
    private String topic;

    private DefaultMQPushConsumer consumerA;

    @Autowired
    MtimePromoStockMapper mtimePromoStockMapper;


    @PostConstruct
    public void initConsumer() {
        consumerA = new DefaultMQPushConsumer(consumerGroup);
        consumerA.setNamesrvAddr(address);
        try {
            consumerA.subscribe(topic,"*");
        } catch (MQClientException e) {
            e.printStackTrace();
            log.info("consumerA订阅失败！！");
        }

        consumerA.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                MessageExt messageExt = msgs.get(0);
                byte[] body = messageExt.getBody();
                String jsonString = new String(body);

                HashMap<String,Integer> hashMap = JSON.parseObject(jsonString, HashMap.class);
                Integer promoId = hashMap.get("promoId");
                Integer amount = hashMap.get("amount");

                Integer affectedRows = mtimePromoStockMapper.updateStockByPid(promoId, amount);
                if (affectedRows < 1) {
                    log.info("消费失败 ！扣减库存失败，promoId:{},amount:{}",promoId,amount);
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        try {
            consumerA.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }

    }

}
