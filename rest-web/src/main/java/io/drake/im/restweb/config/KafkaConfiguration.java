package io.drake.im.restweb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * Date: 2021/04/23/11:37
 *
 * @author : Drake
 * Description:
 */
@Configuration
@EnableKafka
public class KafkaConfiguration {

    @Value("${spring.kafka.consumer.enable-auto-commit}")
    private Boolean autoCommit;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Value("${spring.kafka.consumer.auto-offset-reset}")
    private String autoOffsetReset;

    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String servers;


    public Boolean getAutoCommit() {
        return autoCommit;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getAutoOffsetReset() {
        return autoOffsetReset;
    }

    public String getServers() {
        return servers;
    }
}
