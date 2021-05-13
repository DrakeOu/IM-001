package io.drake.im.client.handler;

import com.google.protobuf.Message;
import io.drake.im.protobuf.generate.IMAck;
import io.drake.im.protobuf.generate.IMMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * Date: 2021/04/21/16:05
 *
 * @author : Drake
 * Description:
 */
@Slf4j
public class ClientToConnectorHandler extends SimpleChannelInboundHandler<Message> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        if(msg instanceof IMAck.Ack){
            //
            IMAck.Ack ack = IMAck.Ack.newBuilder().mergeFrom(msg).build();
            log.debug("recv ack from server, msg[{}]", ack.getExtend());
        }
        if(msg instanceof IMMessage.ChatMsg){
            IMMessage.ChatMsg chatMsg = IMMessage.ChatMsg.newBuilder().mergeFrom(msg).build();
            log.debug("recv msg from friend [{}], msg [{}]", chatMsg.getOriginId(), chatMsg.getContent());

            IMAck.Ack read = IMAck.Ack.newBuilder()
                    .setOriginId(chatMsg.getDestId())
                    .setDestId(chatMsg.getOriginId())
                    .setAckType(IMAck.Ack.AckType.READ)
                    .setCreateTime(new Date().getTime())
                    .setExtend("HAS READ")
                    .build();
            ctx.channel().writeAndFlush(read);
        }
    }

}
