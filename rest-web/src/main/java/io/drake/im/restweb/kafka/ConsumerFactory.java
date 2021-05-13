package io.drake.im.restweb.kafka;

import io.drake.im.restweb.kafka.consumer.AbstractConsumer;
import io.drake.im.restweb.kafka.consumer.GroupMsgConsumer;
import io.drake.im.restweb.kafka.consumer.SingleMsgConsumer;
import io.drake.im.restweb.service.MsgService;

/**
 * Date: 2021/04/23/12:08
 *
 * @author : Drake
 * Description:
 */
public class ConsumerFactory {

    public static SingleMsgConsumer createSingleConsumerThread(ConsumerGroup consumerGroup, AbstractConsumer.MsgConsumer consumerFunc){
        return new SingleMsgConsumer(consumerGroup, consumerFunc);
    }

    public static GroupMsgConsumer createGroupConsumerThread(ConsumerGroup consumerGroup, AbstractConsumer.MsgConsumer consumerFunc){
        return new GroupMsgConsumer(consumerGroup, consumerFunc);
    }

}
