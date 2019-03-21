# 阿里云 ONS Spring boot starter 

## 1.yml配置

在application.yml文件中进行配置:
```javascript
ali:
    ons:
      rocketmq:
        config:
          AccessKey: xxx (必填)        // ONS用户的access Key
          SecretKey: xxx (必填)        // ONS用户的secret Key
          ONSAddr: xxx (选填)          // ONS服务地址
          
          MessageModel: CLUSTERING      // 消息订阅方式:BROADCASTING,CLUSTERING(默认) 
          ProducerId: GID_P
          ConsumerId: GID_C
```


## 2.生产者示例
```java
  //例如，订单创建场景
  @Component
  @MessageConsumer(topic = OnsTopic.ORDER, tag = OnsTag.CREATE)
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


