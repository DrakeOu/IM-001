package io.drake.im.restweb.websocket;

import io.drake.im.common.serializer.JSONSerializer;
import io.drake.im.restweb.domain.ws.WSEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Date: 2021/05/09/20:14
 *
 * @author : Drake
 * Description:
 */
@ServerEndpoint("/u/{userId}")
@Component
@Slf4j
public class WebsocketServer {


    private static Map<String, WebsocketServer> sessionMap = new ConcurrentHashMap<>();

    private static AtomicInteger onlineCount = new AtomicInteger(0);

    public static WebsocketServer getByUserId(String userId){
        if(sessionMap.containsKey(userId)){
            return sessionMap.get(userId);
        }
        log.debug("USER {} WS IS NOT ONLINE", userId);
        return null;
    }


    private Session session;

    @OnOpen
    public void onOpen(Session session, @PathParam("userId")String userId) {
        this.session = session;
        sessionMap.put(userId, this);
        log.debug("USER: {} CONNECT ON WS, NOW WS ONLINE IS {}", userId, onlineCount.incrementAndGet());
    }


    @OnClose
    public void onClose(Session session) {
        //todo 如何从map中删除session
        log.info("SESSION:{} CLOSE", session.getId());
    }



    /**
     * TODO: 发生错误时调用
     *
     * @return
     * @throws
     * @author zhaoxi
     * @time 2018/12/4 16:40
     * @params
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.info("发生错误");
        error.printStackTrace();
    }

    public void sendEventToClient(String eventText){
        session.getAsyncRemote().sendText(eventText);
    }




}
