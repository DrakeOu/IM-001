package io.drake.im.restweb.kafka.consumer;

import com.google.protobuf.Message;
import io.drake.im.common.exception.IMException;
import io.drake.im.common.serializer.JSONSerializer;
import io.drake.im.protobuf.generate.IMMessage;
import kafka.utils.ShutdownableThread;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;

/**
 * Date: 2021/05/11/19:53
 *
 * @author : Drake
 * Description:
 */
@Slf4j
public abstract class AbstractConsumer extends ShutdownableThread {

    protected KafkaConsumer<String, String> consumer;

    protected String groupId;
    protected String topic;

    private MsgConsumer consumerFunction;

    public AbstractConsumer(String name, boolean isInterruptible, MsgConsumer consumerFunction) {
        super(name, isInterruptible);
        this.consumerFunction = consumerFunction;
    }

    @Override
    public void doWork() {
        consumer.subscribe(Collections.singletonList(topic));
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(10));

        for(ConsumerRecord<String, String> record: records){
            log.debug("Thread:{} process mq key-{}, value-{}", currentThread().getName(), record.key(), record.value());
            try {

                consumerFunction.consumer(record.value());

            } catch (IMException e) {
                e.printStackTrace();
            }
        }
    }

    @FunctionalInterface
    public interface MsgConsumer{

        void consumer(String s);

    }
}
