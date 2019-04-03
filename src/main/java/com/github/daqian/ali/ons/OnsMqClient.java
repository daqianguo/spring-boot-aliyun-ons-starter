package com.github.daqian.ali.ons;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
import com.aliyun.openservices.shade.com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 通用消息生产者服务
 *
 * @author daqian
 * @date 2019/3/21 14:56
 */
@Component
@Slf4j
public class OnsMqClient {

    @Autowired
    private ProducerBean producerBean;

    /**
     * 发送消息
     *
     * @param message
     * @return boolean
     * @author daqian
     * @date 2019/4/2 11:36
     */
    public boolean sendMsg(Message message) {
        if (!producerBean.isStarted()) {
            log.warn("Send mq message failed.The producer not started!");
            throw new RuntimeException("Send mq message failed.The producer not started!");
        }

        SendResult sendResult;
        try {
            sendResult = producerBean.send(message);
            if (sendResult != null) {
                log.info(" Send mq message success. Topic is:" + message.getTopic() + " msgId is: " + sendResult.getMessageId());
            }

        } catch (Exception e) {
            log.error(" Send mq message failed. Topic is:" + message.getTopic());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 发送消息
     * 序列化方式：JSON.toJSONBytes(body)
     *
     * @param topic
     * @param tags
     * @param body
     * @return boolean
     * @author daqian
     * @date 2019/4/2 11:37
     */
    public boolean sendMsg(String topic, String tags, Object body) {
        Message message = new Message();
        message.setTopic(topic);
        message.setTag(tags);
        message.setBody(JSON.toJSONBytes(body));
        return sendMsg(message);
    }

    /**
     * 发送消息
     *
     * @param topic
     * @param tags
     * @param body
     * @return boolean
     * @author daqian
     * @date 2019/4/2 11:37
     */
    public boolean sendMsg(String topic, String tags, byte[] body) {
        Message message = new Message();
        message.setTopic(topic);
        message.setTag(tags);
        message.setBody(body);
        return sendMsg(message);
    }
}
