package io.drake.im.transfer.handler;

import com.google.protobuf.Message;
import io.drake.im.protobuf.generate.IMInternal;
import io.drake.im.protobuf.generate.IMMessage;
import io.drake.im.transfer.MsgProducer;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * Date: 2021/04/22/11:51
 *
 * @author : Drake
 * Description: 处理未能直接发送的消息，以及需要持久化的已发送消息
 */
@Slf4j
@ChannelHandler.Sharable
public class ConnectorHandler extends SimpleChannelInboundHandler<Message> {


    public ConnectorHandler(){
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {

        if(null == msg){
            return;
        }
        if(msg instanceof IMMessage.ChatMsg){
            IMMessage.ChatMsg singleMsg = (IMMessage.ChatMsg) msg;
            log.debug("recv single msg");
            if(!singleMsg.getIsSigned()){
                //todo check whether user is online now and resend msg if true
                //if true write back from current channel
            }
            MsgProducer.produce(msg);

        }
        if(msg instanceof IMInternal.Internal){
            log.debug("recv ack, echo");
            ctx.channel().writeAndFlush(msg);
        }
    }
}
