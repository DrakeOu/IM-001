package io.drake.im.restweb.kafka.consumer;

import io.drake.im.restweb.kafka.ConsumerGroup;
import org.apache.kafka.clients.consumer.KafkaConsumer;

/**
 * Date: 2021/05/11/20:10
 *
 * @author : Drake
 * Description:
 */
public class GroupMsgConsumer extends AbstractConsumer {
    public GroupMsgConsumer(ConsumerGroup consumerGroup, MsgConsumer consumerFunction) {
        super("", false, consumerFunction);
        consumer = new KafkaConsumer<>(consumerGroup.getProps());
        groupId = consumerGroup.getGroupId();
        topic = consumerGroup.getTopic();
    }
}
