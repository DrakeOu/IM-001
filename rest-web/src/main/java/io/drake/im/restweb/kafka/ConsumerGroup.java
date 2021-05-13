package io.drake.im.restweb.kafka;

import io.drake.im.restweb.config.KafkaConfiguration;
import lombok.Data;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Properties;

/**
 * Date: 2021/04/23/11:34
 *
 * @author : Drake
 * Description: 接收初始化配置，用于创建指定topic的consumer,类似factory
 */
@Data
public class ConsumerGroup {

    private Properties props;

    private String groupId;

    private String topic;

    public ConsumerGroup(KafkaConfiguration configuration, String topic){
        initProps(configuration);
        this.groupId = configuration.getGroupId();
        this.topic = topic;
    }

    private void initProps(KafkaConfiguration configuration){
        props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, configuration.getServers());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, configuration.getAutoOffsetReset());
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, configuration.getAutoCommit());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, configuration.getGroupId());
    }


}
