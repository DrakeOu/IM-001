package io.drake.im.connector;

import io.drake.im.common.codec.MsgDecoder;
import io.drake.im.common.codec.MsgEncoder;
import io.drake.im.connector.handler.ConnectorToTransferHandler;
import io.drake.im.protobuf.generate.IMInternal;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Date: 2021/04/22/16:59
 *
 * @author : Drake
 * Description:
 */
@Slf4j
public class TransferClient {

    private ChannelFuture channelFuture;

    public void start(){
        EventLoopGroup loopGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        ChannelFuture future = bootstrap.group(loopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new MsgEncoder());
                        pipeline.addLast(new MsgDecoder());
                        pipeline.addLast(new ConnectorToTransferHandler());

                    }
                }).connect("localhost", 6062).addListener((ChannelFutureListener) future1 -> {
                    if(future1.isSuccess()){
                        log.debug("successfully connect to server");
                    }else{
                        log.error("fail connect to netty server");
                    }
                });

        try {
            future.get(10, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }
        channelFuture = future;

        greetToTransfer();
    }

    private void greetToTransfer(){
        IMInternal.Internal greet = IMInternal.Internal.newBuilder()
                .setMsgType(IMInternal.Internal.MsgType.GREET)
                .setOrigin(IMInternal.Internal.Module.CONNECTOR)
                .setDest(IMInternal.Internal.Module.TRANSFER)
                .setBody("Greet from connector")
                .build();
        channelFuture.channel().writeAndFlush(greet);
    }
}
