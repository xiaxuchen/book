package org.originit.notification;

import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.originit.notification.domain.ClearShopCarEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xxc
 */
@SpringBootApplication
@RestController
public class NotificationApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationApplication.class, args);
    }

    @Resource(name = "extRocketMQTemplate")
    private RocketMQTemplate extRocketMQTemplate;

    @Value("${demo.rocketmq.shopcarTopic}")
    private String shopcarTopic;

    volatile AtomicInteger id = new AtomicInteger(0);

    @GetMapping("/sendMsg/{msg}")
    public String send(@PathVariable String msg) {
        final SendResult sendResult = extRocketMQTemplate.syncSend(shopcarTopic, new ClearShopCarEvent(id.incrementAndGet(), msg), 100000);
        return sendResult.toString();
    }
}
