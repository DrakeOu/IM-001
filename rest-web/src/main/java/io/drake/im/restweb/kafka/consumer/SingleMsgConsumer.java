package io.drake.im.restweb.kafka.consumer;

import io.drake.im.common.exception.IMException;
import io.drake.im.common.serializer.JSONSerializer;
import io.drake.im.protobuf.generate.IMMessage;
import io.drake.im.restweb.kafka.ConsumerGroup;
import io.drake.im.restweb.service.MsgService;
import kafka.utils.ShutdownableThread;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;

/**
 * Date: 2021/04/23/11:08
 *
 * @author : Drake
 * Description: 接收一个自定义的consumer，并封装成task，用于执行doWork方法
 */
@Slf4j
public class SingleMsgConsumer extends AbstractConsumer {


    public SingleMsgConsumer(ConsumerGroup consumerGroup, MsgConsumer consumerFunction) {
        super("", false, consumerFunction);
        consumer = new KafkaConsumer<>(consumerGroup.getProps());
        groupId = consumerGroup.getGroupId();
        topic = consumerGroup.getTopic();
    }

}
