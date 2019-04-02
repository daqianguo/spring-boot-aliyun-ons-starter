# 阿里云 ONS - Spring boot starter 

## 1.yml配置

在application.yml文件中进行配置:
```javascript
ali:
    ons:
      rocketmq:
        consumer:
          enable: false
        producer:
          enable: false
        config:
          AccessKey: xxx            // ONS用户的access Key
          SecretKey: xxx            // ONS用户的secret Key
          ONSAddr: xxx              // ONS服务地址
          MessageModel: CLUSTERING  // 消息订阅方式:BROADCASTING,CLUSTERING(默认) 
          GROUP_ID: GID
          ...    //更多config配置见下方
```

> config配置同阿里云官方(update on 20190325)
<br>最新配置及说明，详见阿里云官方文档：https://help.aliyun.com/document_detail/93574.html?spm=a2c4g.11186623.6.553.8185447dHy2atL

config参数|类型|是否必填|默认值|说明
---|---|---|---|---
AccessKey	                |String	|是|-	        |您在阿里云账号管理控制台中创建的 AccessKeyId，用于身份认证。
SecretKey	                |String	|是|-	        |您在阿里云账号管理控制台中创建的 AccessKeySecret，用于身份认证。
OnsChannel	                |String	|否|ALIYUN	    |用户渠道，阿里云：ALIYUN，聚石塔用户为：CLOUD。
NAMESRV_ADDR	            |String	|否|-	        |设置 TCP 协议接入点。
GROUP_ID	                |String	|否|-	        |Consumer 实例的唯一 ID，您在控制台创建的 Group ID。
MessageModel	            |String	|否|CLUSTERING	|设置 Consumer 实例的消费模式，集群消费：CLUSTERING，广播消费：BROADCASTING。
ConsumeThreadNums	        |String	|否|64	        |消费线程数量。
MaxReconsumeTimes	        |String	|否|16	        |设置消息消费失败的最大重试次数。
ConsumeTimeout	            |String	|否|15	        |设置每条消息消费的最大超时时间，超过设置时间则被视为消费失败，等下次重新投递再次消费。每个业务需要设置一个合理的值，单位：分钟（min）。
ConsumeMessageBatchMaxSize	|String	|否|1	        |BatchConsumer每次批量消费的最大消息数量，默认值为1，允许自定义范围为[1, 32]，实际消费数量可能小于该值。
CheckImmunityTimeInSeconds	|String	|否|30	        |设置事务消息第一次回查的最快时间，单位：秒（s）。
suspendTimeMillis	        |String	|否|3000        | 只适用于顺序消息，设置消息消费失败的重试间隔时间，单位：毫秒（ms）。
ONSAddr         	        |String	|是|-           | 只适用于顺序消息，设置消息消费失败的重试间隔时间，单位：毫秒（ms）。


## 2.生产者示例
```java
  //例如，订单创建场景
  @Autowired
  private OnsMqClient onsMqClient;

  @GetMapping("test")
  public JSONResult<Object> test() {
      Message message = new Message();
      message.setTopic("ORDER");
      message.setTag("CREATE");
      message.setBody("有新订单创建".getBytes());
      onsMqClient.sendMsg(message);
      return JSONResult.ok();
  }
```

## 3.消费者示例
```java
  //例如，订单创建场景
  @Component
  @MessageConsumer(topic = "ORDER", tag = "CREATE")
  public class MyMessageListener implements MessageListener{
        @Override
        public Action consume(Message message, ConsumeContext consumeContext) {
            log.info("收到消息：{}", message);
            String body = new String(message.getBody());
            String tag = message.getTag();
            
            try {
                // do something ....
                
                //确认消息已经消费
                return Action.CommitMessage;
            } catch (Exception e) {
                //稍后重新消费
                return Action.ReconsumeLater;
            }
        }
    
  }
```


