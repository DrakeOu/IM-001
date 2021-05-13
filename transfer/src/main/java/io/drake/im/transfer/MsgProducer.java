package io.drake.im.transfer;

import com.google.protobuf.Message;
import io.drake.im.common.exception.IMException;
import io.drake.im.common.constant.ImConstant;
import io.drake.im.common.serializer.JSONSerializer;
import io.drake.im.protobuf.generate.IMMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.protocol.types.Field;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * Date: 2021/04/23/10:50
 *
 * @author : Drake
 * Description:
 */
@Slf4j
public class MsgProducer {

    private static final String BROKER_LIST = "localhost:9092";
    private static final String SINGLE_TOPIC = ImConstant.offlineTopic;
    private static final String GROUP_TOPIC = ImConstant.groupOfflineTopic;

    private static KafkaProducer<String, String> producer;
    private static AdminClient adminClient;

    static {
        producer = new KafkaProducer<>(initConfig());
        adminClient = KafkaAdminClient.create(initAdminConfig());
        adminClient.createTopics(Collections.singleton(new NewTopic(SINGLE_TOPIC, 1, (short) 1)));
        adminClient.close();
    }

    private static Properties initAdminConfig(){
        Properties properties = new Properties();
        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, BROKER_LIST);
        return properties;
    }

    private static Properties initConfig(){
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BROKER_LIST);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return properties;
    }

    public static void produce(Message msg){
        try {
            if(msg instanceof IMMessage.ChatMsg){
                IMMessage.ChatMsg chatMsg = (IMMessage.ChatMsg) msg;
                String value = JSONSerializer.serializeProtoBuf(chatMsg);

                producer.send(new ProducerRecord<>(chooseTopic(chatMsg), value), (recordMetaData, e) -> {
                    if(null != e){
                        log.error("msg send error");
                    }else{
                        log.debug("offset:{}, partition:{}", recordMetaData.offset(), recordMetaData.partition());
                    }
                });

            }
        } catch (IMException e) {
            log.error("produce mq error due to json serialize error");
            e.printStackTrace();
        }
    }

    private static String chooseTopic(IMMessage.ChatMsg chatMsg){
        if(chatMsg.getType() == IMMessage.ChatMsg.MessageType.SINGLE){
            return SINGLE_TOPIC;
        }else if(chatMsg.getType() == IMMessage.ChatMsg.MessageType.GROUP){
            return GROUP_TOPIC;
        }else{
            throw new IMException("UNKNOWN MSG TYPE");
        }
    }

    public static void main(String[] args) {
        ProducerRecord<String, String> record = new ProducerRecord<>(SINGLE_TOPIC, "hello");
        List<Integer> list = new ArrayList<>(10);
        producer.send(record, (recordMetadata, e) -> {
            if(null != e){
                log.error("msg send error");
            }else{
                log.debug("offset:{}, partition:{}", recordMetadata.offset(), recordMetadata.partition());
            }
        });
    }
}
