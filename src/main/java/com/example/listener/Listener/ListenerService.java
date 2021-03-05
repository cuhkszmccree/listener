package com.example.listener.Listener;


import com.example.listener.Config.Constant;
import com.example.listener.Service.Handler;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ListenerService {
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    Handler handler;
    @RabbitListener(queues = Constant.Queue1)                //测试死信队列相关
    public void MessageHandler1(Message message, Channel channel) throws IOException {
        String messageID = message.getMessageProperties().getMessageId();
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            int i = 1 / 0;
            if (messageID != null && !redisTemplate.hasKey(messageID)) {
                handler.handle_Q1(new String(message.getBody()));
                redisTemplate.opsForValue().set(messageID, true, 10000);
                channel.basicAck(deliveryTag, false);
            } else {
                channel.basicReject(deliveryTag, false);
            }
        }
        catch(Exception e){
            channel.basicReject(deliveryTag, false);
        }
    }

    @RabbitListener(queues = Constant.Queue2)                //测试重试队列相关
    public void MessageHandler2(Message message, Channel channel) throws IOException {
        String messageID = message.getMessageProperties().getMessageId();
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        System.out.println("重试开始");
        int i = 1/0;
        if(messageID != null &&  !redisTemplate.hasKey(messageID)) {
            handler.handle_Q2(new String(message.getBody()));
            redisTemplate.opsForValue().set(messageID,true,10000);
            channel.basicAck(deliveryTag, false);
        }
        else{
            channel.basicReject(deliveryTag, false);
        }
    }

    @RabbitListener(queues = Constant.Queue3)
    public void MessageHandler3(Message message, Channel channel) throws IOException {
        String messageID = message.getMessageProperties().getMessageId();
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        if(messageID != null &&  !redisTemplate.hasKey(messageID)) {
            handler.handle_Q3(new String(message.getBody()));
            redisTemplate.opsForValue().set(messageID,true,10000);
            channel.basicAck(deliveryTag,false);
        }
        else{
            channel.basicReject(deliveryTag, false);
        }
    }

    @RabbitListener(queues = Constant.Queue4)
    public void MessageHandler4(Message message, Channel channel) throws IOException {
        String messageID = message.getMessageProperties().getMessageId();
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        if(messageID != null &&  !redisTemplate.hasKey(messageID)) {
            handler.handle_Q4(new String(message.getBody()));
            redisTemplate.opsForValue().set(messageID, true, 10000);
            channel.basicAck(deliveryTag, false);
        }
        else{
            channel.basicReject(deliveryTag, false);
        }
    }

    @RabbitListener(queues = Constant.Retry_Queue)    //重试消费者
    public void Retry(Message message, Channel channel) throws IOException {
        String msg = new String(message.getBody());
        System.out.println("重试消费者收到了=-=" + msg);
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        channel.basicAck(deliveryTag,false);
    }

    @RabbitListener(queues = Constant.Dead_Queue)      //死信消费者
    public void Dead( Message message, Channel channel) throws IOException {
        String msg = new String(message.getBody());
        System.out.println("死信队列收到了=-=" + msg);
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        channel.basicAck(deliveryTag,false);
    }
}
