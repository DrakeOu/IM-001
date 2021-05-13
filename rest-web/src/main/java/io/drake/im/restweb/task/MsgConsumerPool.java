package io.drake.im.restweb.task;

import io.drake.im.common.constant.ImConstant;
import io.drake.im.common.serializer.JSONSerializer;
import io.drake.im.protobuf.generate.IMMessage;
import io.drake.im.restweb.config.KafkaConfiguration;
import io.drake.im.restweb.kafka.ConsumerFactory;
import io.drake.im.restweb.kafka.ConsumerGroup;
import io.drake.im.restweb.kafka.consumer.GroupMsgConsumer;
import io.drake.im.restweb.kafka.consumer.SingleMsgConsumer;
import io.drake.im.restweb.service.GroupMsgService;
import io.drake.im.restweb.service.MsgService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Date: 2021/04/23/13:28
 *
 * @author : Drake
 * Description:
 */
@Component
public class MsgConsumerPool {

    private ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 2, 100, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

    private final KafkaConfiguration configuration;

    public MsgConsumerPool(KafkaConfiguration configuration, MsgService msgService, GroupMsgService groupMsgService) {
        this.configuration = configuration;
        this.msgService = msgService;
        this.groupMsgService = groupMsgService;
    }

    private final MsgService msgService;
    private final GroupMsgService groupMsgService;


    private ConsumerGroup consumerGroup;

    @PostConstruct
    public void initPool(){
        consumerGroup = new ConsumerGroup(configuration, ImConstant.offlineTopic);
        SingleMsgConsumer msgConsumer = ConsumerFactory.createSingleConsumerThread(consumerGroup, (msg) -> {
            IMMessage.ChatMsg.Builder builder = IMMessage.ChatMsg.newBuilder();
            JSONSerializer.deserializeProtoBuf(msg, builder);
            msgService.saveMsg(builder.build());
        });


        ConsumerGroup group = new ConsumerGroup(configuration, ImConstant.groupOfflineTopic);
        GroupMsgConsumer groupConsumerThread = ConsumerFactory.createGroupConsumerThread(group, (msg) -> {
            IMMessage.ChatMsg.Builder builder = IMMessage.ChatMsg.newBuilder();
            JSONSerializer.deserializeProtoBuf(msg, builder);
            groupMsgService.saveGroupMsg(builder.build());
        });

        executor.execute(groupConsumerThread);
        executor.execute(msgConsumer);
    }

}
