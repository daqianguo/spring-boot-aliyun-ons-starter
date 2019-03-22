package com.daqian.ali.ons;

import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.bean.Subscription;
import com.daqian.ali.ons.annotation.MessageConsumer;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: daqian
 * @Date: 2019/3/20 20:13
 */
@Component
public class MessageListenerHandler implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        MessageListenerHandler.applicationContext = applicationContext;
    }

    private static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 获取所有实现的消费者监听
     *
     * @return subscriptionTable
     * @throws BeansException
     * @author daqian
     * @date 2019/3/21 15:46
     */
    public static Map<Subscription, MessageListener> getSubscriptionTable() throws BeansException {
        try {
            String[] messageConsumerBeans = getApplicationContext().getBeanNamesForAnnotation(MessageConsumer.class);
            Map<Subscription, MessageListener> subscriptionTable = new HashMap<>(messageConsumerBeans.length);
            for (String beanName : messageConsumerBeans) {
                Class clazz = applicationContext.getType(beanName);
                MessageConsumer messageConsumer = AnnotationUtils.findAnnotation(clazz, MessageConsumer.class);

                //绑定监听的topic
                Subscription subscription = new Subscription();
                subscription.setTopic(messageConsumer.topic());
                //绑定要监听的tag，多个tag用 || 隔开
                subscription.setExpression(messageConsumer.tag());

                subscriptionTable.put(subscription, (MessageListener) applicationContext.getBean(beanName));

            }
            return subscriptionTable;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
