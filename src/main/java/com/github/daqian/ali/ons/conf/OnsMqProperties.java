package com.github.daqian.ali.ons.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;
import java.util.Properties;

/**
 * ons配置参数
 * 参考：https://help.aliyun.com/document_detail/93574.html?spm=a2c4g.11186623.6.553.927d650eeh6vzK
 * <p>
 * AccessKey	                String	-	        您在阿里云账号管理控制台中创建的 AccessKeyId，用于身份认证。
 * SecretKey	                String	-	        您在阿里云账号管理控制台中创建的 AccessKeySecret，用于身份认证。
 * OnsChannel	                String	ALIYUN	    用户渠道，阿里云：ALIYUN，聚石塔用户为：CLOUD。
 * NAMESRV_ADDR	                String	-	        设置 TCP 协议接入点。
 * GROUP_ID	                    String	-	        Consumer 实例的唯一 ID，您在控制台创建的 Group ID。
 * MessageModel	                String	CLUSTERING	设置 Consumer 实例的消费模式，集群消费：CLUSTERING，广播消费：BROADCASTING。
 * ConsumeThreadNums	        String	64	        消费线程数量。
 * MaxReconsumeTimes	        String	16	        设置消息消费失败的最大重试次数。
 * ConsumeTimeout	            String	15	        设置每条消息消费的最大超时时间，超过设置时间则被视为消费失败，等下次重新投递再次消费。每个业务需要设置一个合理的值，单位：分钟（min）。
 * ConsumeMessageBatchMaxSize	String	1	        BatchConsumer每次批量消费的最大消息数量，默认值为1，允许自定义范围为[1, 32]，实际消费数量可能小于该值。
 * CheckImmunityTimeInSeconds	String	30	        设置事务消息第一次回查的最快时间，单位：秒（s）。
 * suspendTimeMillis	        String	3000        只适用于顺序消息，设置消息消费失败的重试间隔时间，单位：毫秒（ms）。
 * </p>
 *
 * @author daqian
 * @date 2019/3/21 14:52
 */
@ConfigurationProperties(prefix = "ali.ons.rocketmq")
public class OnsMqProperties implements Serializable {

    private Properties config;

    public Properties getConfig() {
        return config;
    }

    public void setConfig(Properties config) {
        this.config = config;
    }
}
