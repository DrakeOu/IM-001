package io.drake.im.restweb.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.drake.im.common.serializer.JSONSerializer;
import io.drake.im.restweb.domain.ws.WSEvent;
import io.drake.im.restweb.service.WebSocketService;
import io.drake.im.restweb.websocket.WebsocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Date: 2021/05/09/15:30
 *
 * @author : Drake
 * Description: 依赖关系 wsService -> other service
 */
@Service
@Slf4j
public class WebSocketServiceImpl implements WebSocketService {


    //如果对方不在线，需要持久化之后处理
    @Override
    public void sendEvent(String userId, WSEvent event) {
        try {
            WebsocketServer websocketServer = WebsocketServer.getByUserId(userId);
            if(null != websocketServer) {
                websocketServer.sendEventToClient(JSONSerializer.serializeToStr(event));
            }
        } catch (JsonProcessingException e) {
            log.error("JSON PARSE ERROR DURING WS SERVICE");
            e.printStackTrace();
        }
    }

    @Override
    public void sendGroupEvent(String groupId, WSEvent event) {
        System.out.println("NOT READY YET");
    }
}
