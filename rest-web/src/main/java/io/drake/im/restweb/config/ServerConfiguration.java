package io.drake.im.restweb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Date: 2021/05/24/14:28
 *
 * @author : Drake
 * Description:
 */
@Configuration
public class ServerConfiguration {

    @Value("${server.netty.host}")
    private String nettyServerHost;

    @Value("${server.netty.port}")
    private String nettyServerPort;

    private static final Integer DEFAULT_NETTY_PORT = 6061;

    @Value("${server.ws.address}")
    private String wsServerAddr;

    public String getNettyServerHost() {
        return nettyServerHost;
    }

    public Integer getNettyServerPort() {
        try{
            return Integer.parseInt(nettyServerPort);
        } catch (NumberFormatException e){
            return DEFAULT_NETTY_PORT;
        }
    }

    public String getWsServerAddr() {
        return wsServerAddr;
    }
}
