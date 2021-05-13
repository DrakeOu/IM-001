package io.drake.im.restweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * Date: 2021/05/09/15:18
 *
 * @author : Drake
 * Description:
 */
@Configuration
@EnableWebSocket
public class WebSocketConfiguration {

    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }

}
