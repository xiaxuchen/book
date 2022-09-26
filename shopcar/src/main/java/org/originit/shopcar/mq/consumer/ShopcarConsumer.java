package org.originit.shopcar.mq.consumer;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.originit.shopcar.domain.ClearShopCarEvent;
import org.springframework.stereotype.Component;


/**
 * @author xxc
 */
@Component
@RocketMQMessageListener(topic = "${demo.rocketmq.shopcarTopic}", consumerGroup = "shop-car-consumer")
public class ShopcarConsumer implements RocketMQListener<ClearShopCarEvent> {

    boolean refuse = true;

    @Override
    public void onMessage(ClearShopCarEvent clearShopCarEvent) {
        try {
            System.out.println(System.currentTimeMillis());
            Thread.sleep(3000);
            System.out.println(System.currentTimeMillis());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (refuse) {
            refuse = false;
            throw new IllegalStateException("当前不对哦");
        } else {
            refuse = true;
        }
        System.out.println(clearShopCarEvent);

    }
}
