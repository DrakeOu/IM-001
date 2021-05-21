package io.drake.im.transfer;

import io.drake.im.common.codec.MsgDecoder;
import io.drake.im.common.codec.MsgEncoder;
import io.drake.im.transfer.handler.ConnectorHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Date: 2021/04/22/11:47
 *
 * @author : Drake
 * Description:
 */
@Slf4j
public class TransferServer {


    private ChannelFuture channelFuture;

    public TransferServer() {
    }

    public void start(){
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new MsgEncoder());
                        pipeline.addLast(new MsgDecoder());
                        pipeline.addLast(new ConnectorHandler());
                    }
                });
        int port = 6062;
        ChannelFuture future = bootstrap.bind("transfer", port).addListener((ChannelFutureListener) f -> {
            if(f.isSuccess()){
                log.debug("bind to port [{}] success", port);
            }else{
                log.error("bind to [{}] failed", port);
            }
        });

        try {
            future.get(10, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }
        this.channelFuture = future;
    }

}
