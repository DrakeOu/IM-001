package io.drake.im.connector.handler;

import com.google.protobuf.Message;
import io.drake.im.connector.service.TransferClientService;
import io.drake.im.protobuf.generate.IMInternal;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 2021/04/22/17:00
 *
 * @author : Drake
 * Description:
 */
@Slf4j
public class ConnectorToTransferHandler extends SimpleChannelInboundHandler<Message> {

    private TransferClientService transferClientService;
    //no need a context of transfer, its stateless
    private static List<ChannelHandlerContext> transferCtxs = new ArrayList<>();

    public ConnectorToTransferHandler(){
        this.transferClientService = new TransferClientService();
    }

    public static ChannelHandlerContext getOneTransferCtx(){
        if(transferCtxs.isEmpty()){
            return null;
        }
        return transferCtxs.get((int) System.currentTimeMillis() & transferCtxs.size() - 1);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        if(null == msg){
            return;
        }
        if(msg instanceof IMInternal.Internal){
            log.debug("recv ack from transfer");
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        transferCtxs.add(ctx);
        super.channelActive(ctx);
    }
}
