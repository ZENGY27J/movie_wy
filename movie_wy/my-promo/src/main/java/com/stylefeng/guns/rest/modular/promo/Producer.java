package com.stylefeng.guns.rest.modular.promo;

import com.alibaba.fastjson.JSON;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeStockLogMapper;
import com.stylefeng.guns.rest.common.persistence.model.MtimeStockLog;
import com.wuyan.promo.PromoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;
import java.util.HashMap;

/**
 * @Program: movie_wy
 * @Author: ZyEthan
 * @Description: 消息生产者
 * @Date: 2019-10-19-15:24
 */
@Component
@Slf4j
public class Producer {

    @Value("${mq.nameserver.address}")
    private String address;

    @Value("${mq.producergroup}")
    private String producerGroup;

    @Value("${mq.topic}")
    private String topic;

    @Value("${mq.transactionproducergroup}")
    private String transactiongroup;

    private DefaultMQProducer producerA;
    /**
     * MQ事务型消息生产者
     */
    private TransactionMQProducer transactionMQProducer;

    @Autowired
    PromoService promoService;
    @Autowired
    MtimeStockLogMapper mtimeStockLogMapper;

    @PostConstruct
    public void initProducer() {
//        producerA = new DefaultMQProducer(producerGroup);
//        producerA.setNamesrvAddr(address);
//        try {
//            producerA.start();
//        } catch (MQClientException e) {
//            e.printStackTrace();
//        }
//        log.info("producerA启动成功了!");

        transactionMQProducer = new TransactionMQProducer(transactiongroup);
        transactionMQProducer.setNamesrvAddr(address);

        try {
            transactionMQProducer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
        log.info("transactionMQProducer 启动成功...");

        /**
         * 发送事务型消息的生产者
         */
        transactionMQProducer.setTransactionListener(new TransactionListener() {
            /**
             * 执行本地事务
             * @param message
             * @param args  （参数来自transactionMQProducer.sendMessageInTransaction(message,argsMap);）
             * @return
             */
            @Override
            public LocalTransactionState executeLocalTransaction(Message message, Object args) {
                if (args == null) {
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                }
                HashMap<String, Object> argsMap = (HashMap<String, Object>) args;
                Integer promoId = (Integer) argsMap.get("promoId");
                Integer userId = (Integer) argsMap.get("userId");
                Integer amount = (Integer) argsMap.get("amount");
                String stockLogId = (String) argsMap.get("stockLogId");

                boolean order = false;
                try {
                    // 执行本地事务
                    order = promoService.createOrder(promoId, userId, amount, stockLogId);
                } catch (Exception e) {
                    e.printStackTrace();
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                }
                if (!order) {
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                }
                return LocalTransactionState.COMMIT_MESSAGE;
            }

            /**
             * 根据库存流水状态的情况
             * 1，库存流水状态是成功
             * 返回COMMIT_MESSAGE
             * 2，库存流水状态是失败
             * 返回ROLLBACK_MESSAGE
             * 3，库存流水状态是初始化的
             * 返回 UNKNOWN
             */
            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
                byte[] body = messageExt.getBody();
                String bodyStr = new String(body);

                HashMap<String, Object> hashMap = JSON.parseObject(bodyStr, HashMap.class);

                String stockLogId = (String) hashMap.get("stockLogId");
                MtimeStockLog mtimeStockLog = mtimeStockLogMapper.selectById(stockLogId);
                Integer status = mtimeStockLog.getStatus();

                if (StockLogStatus.SUCCESS.getIndex() == status) {
                    return LocalTransactionState.COMMIT_MESSAGE;
                }else if (StockLogStatus.FAIL.getIndex() == status) {
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                }else {
                    return LocalTransactionState.UNKNOW;
                }
            }
        });
    }

    /**
     * 异步发送消息，修改数据库库存（非事务型消息）
     * @param promoId
     * @param amount
     * @return
     */
    public boolean asyncDecreaseStock(Integer promoId, Integer amount) {
        HashMap<String, Integer> hashMap = new HashMap<>();
        hashMap.put("promoId",promoId);
        hashMap.put("amount",amount);

        String jsonString = JSON.toJSONString(hashMap);
        Message message = new Message(topic, jsonString.getBytes(Charset.forName("utf-8")));
        SendResult sendResult = null;
        try {
            sendResult = producerA.send(message);
        } catch (MQClientException e) {
            e.printStackTrace();
            return false;
        } catch (RemotingException e) {
            e.printStackTrace();
            return false;
        } catch (MQBrokerException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        if (sendResult == null) {
            return false;
        }
        SendStatus sendStatus = sendResult.getSendStatus();
        if (SendStatus.SEND_OK == sendStatus) {
            return true;
        }
        return false;
    }


    /**
     * 发送事务型消息
     * @param promoId
     * @param amount
     * @param userId
     * @param stockLogId
     * @return
     */
    public Boolean transactionCreateOrder(Integer promoId,Integer amount,Integer userId,String stockLogId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("promoId",promoId);
        hashMap.put("amount",amount);
        hashMap.put("userId",userId);
        hashMap.put("stockLogId",stockLogId);
        String jsonString = JSON.toJSONString(hashMap);

        HashMap<String, Object> argsMap = new HashMap<>();
        argsMap.put("promoId",promoId);
        argsMap.put("amount",amount);
        argsMap.put("userId",userId);
        argsMap.put("stockLogId",stockLogId);

        TransactionSendResult transactionSendResult = null;
        Message message = new Message(topic,jsonString.getBytes(Charset.forName("utf-8")));
        try {
            transactionSendResult = transactionMQProducer.sendMessageInTransaction(message,argsMap);
        } catch (MQClientException e) {
            e.printStackTrace();
            return false;
        }
        if (transactionSendResult == null) {
            return false;
        }
        LocalTransactionState localTransactionState = transactionSendResult.getLocalTransactionState();
        if (LocalTransactionState.COMMIT_MESSAGE.equals(localTransactionState)) {
            return true;
        }

        return false;
    }


    public DefaultMQProducer getProducerA() {
        return this.producerA;
    }
}
