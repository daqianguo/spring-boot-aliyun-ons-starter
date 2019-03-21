package com.daqian.ali.ons;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
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
     * @return
     */
    public boolean sendMsg(Message message) {

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
}
