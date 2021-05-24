package io.drake.im.connector;


import io.drake.im.common.codec.MsgDecoder;
import io.drake.im.common.codec.MsgEncoder;
import io.drake.im.common.service.impl.SessionServiceImpl;
import io.drake.im.connector.config.ConnectorConfiguration;
import io.drake.im.connector.domain.conn.ConnectorConnContext;
import io.drake.im.connector.handler.ConnectorToClientHandler;
import io.drake.im.connector.service.ConnectorClientService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ConnectorServer {
    private static final Logger log = LoggerFactory.getLogger(ConnectorServer.class);

    public static void start(TransferClient transferClient, ConnectorConfiguration connectorConfiguration){
        EventLoopGroup workGroup = new NioEventLoopGroup();
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        ConnectorToClientHandler connectorToClientHandler = new ConnectorToClientHandler(new ConnectorClientService(new ConnectorConnContext(),
                new SessionServiceImpl(connectorConfiguration.getRedisHost(), connectorConfiguration.getRedisPort(), connectorConfiguration.getRedisPassword()),
                transferClient));
        /**
         * 固定拳法
         * bootstrap启动，设置group, 设置连接channel，添加handlerInitializer, 设置option
         */
        ServerBootstrap serverBootstrap = new ServerBootstrap()
                .group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast(new MsgDecoder());
                        pipeline.addLast(new MsgEncoder());
                        pipeline.addLast(connectorToClientHandler);
                    }
                });
        int port = connectorConfiguration.getServerPort();
        ChannelFuture f = serverBootstrap.bind(new InetSocketAddress(port)).addListener((ChannelFutureListener) future -> {
            if(future.isSuccess()){
                log.debug("bind to port [{}] success", port);
            }else{
                log.error("bind to [{}] failed", port);
            }
        });

        try {
            f.get(10, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }


    }

}
