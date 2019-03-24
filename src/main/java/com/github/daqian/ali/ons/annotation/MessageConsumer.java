package com.github.daqian.ali.ons.annotation;


import java.lang.annotation.*;

/**
 * 消费者监听注解
 *
 * @Author: daqian
 * @Date: 2019/3/20 20:13
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface MessageConsumer {

    /**
     * topic
     *
     * @return
     */
    String topic();

    /**
     * tag
     *
     * @return
     */
    String tag() default "*";

}
