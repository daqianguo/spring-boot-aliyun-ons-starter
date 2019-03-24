package com.github.daqian.ali.ons.conf;

import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.bean.ConsumerBean;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
import com.github.daqian.ali.ons.MessageListenerHandler;
import com.github.daqian.ali.ons.OnsMqClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * ali ons rocketMQ
 *
 * @author daqian
 * @date 2019/3/15 11:01
 */
@Configuration
@Slf4j
@EnableConfigurationProperties(OnsMqProperties.class)
public class OnsRocketMqConf {

    @Bean
    public MessageListenerHandler messageListenerHandler() {
        return new MessageListenerHandler();
    }

    @Bean
    public OnsMqClient onsMqClient() {
        return new OnsMqClient();
    }

    /**
     * 生产者是否开启
     */
    @Value("${ali.ons.rocketmq.producer.enable}")
    private Boolean producerEnable;

    /**
     * 消费者是否开启
     */
    @Value("${ali.ons.rocketmq.consumer.enable}")
    private Boolean consumerEnable;


    @Autowired
    private OnsMqProperties onsMqProperties;

    private Properties commonProperties() {
        Properties properties = new Properties();
        // AccessKey 阿里云身份验证，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.AccessKey, onsMqProperties.getConfig().getProperty(PropertyKeyConst.AccessKey));
        // SecretKey 阿里云身份验证，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.SecretKey, onsMqProperties.getConfig().getProperty(PropertyKeyConst.SecretKey));
        // 设置 TCP 接入域名（此处以公共云生产环境为例）
        properties.put(PropertyKeyConst.ONSAddr, onsMqProperties.getConfig().getProperty(PropertyKeyConst.ONSAddr));

        //设置发送超时时间，单位毫秒
        if (onsMqProperties.getConfig().getProperty(PropertyKeyConst.SendMsgTimeoutMillis) != null) {
            properties.setProperty(PropertyKeyConst.SendMsgTimeoutMillis, onsMqProperties.getConfig().getProperty(PropertyKeyConst.SendMsgTimeoutMillis));
        }

        return properties;

    }

    /**
     * 消息生产者
     *
     * @return com.aliyun.openservices.ons.api.bean.ProducerBean
     * @author daqian
     * @date 2019/3/21 14:26
     */
    @Bean(destroyMethod = "shutdown")
    public ProducerBean producerBean() {
        Properties properties = this.commonProperties();
        properties.put(PropertyKeyConst.ProducerId, onsMqProperties.getConfig().getProperty(PropertyKeyConst.ProducerId));

        ProducerBean producerBean = new ProducerBean();
        producerBean.setProperties(properties);
        if (Boolean.TRUE.equals(producerEnable)) {
            producerBean.start();
        }
        return producerBean;
    }

    /**
     * 消息消费者
     *
     * @return com.aliyun.openservices.ons.api.bean.ConsumerBean
     * @author daqian
     * @date 2019/3/21 14:26
     */
    @Bean(destroyMethod = "shutdown")
    public ConsumerBean consumerBean() {
        Properties properties = this.commonProperties();
        properties.put(PropertyKeyConst.ConsumerId, onsMqProperties.getConfig().getProperty(PropertyKeyConst.ConsumerId));
        // 订阅方式 (默认集群)
        properties.put(PropertyKeyConst.MessageModel, onsMqProperties.getConfig().getProperty(PropertyKeyConst.MessageModel));

        ConsumerBean consumerBean = new ConsumerBean();
        consumerBean.setProperties(properties);
        //将所有实现的消费者监听加入订阅关系
        consumerBean.setSubscriptionTable(MessageListenerHandler.getSubscriptionTable());
        if (Boolean.TRUE.equals(consumerEnable)) {
            consumerBean.start();
        }
        return consumerBean;
    }
}
