package com.example.demo;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

/**
 * Date: 2021/05/09/20:27
 *
 * @author : Drake
 * Description:
 */
@ServerEndpoint("/u")
@Component
public class WSServer {


    /**
     * TODO: 连接建立成功调用的方法
     *
     * @return
     * @throws
     * @author zhaoxi
     * @time 2018/12/4 16:32
     * @params
     */
    @OnOpen
    public void onOpen(Session session) {
        System.out.println("11111111111111111");
    }

    /**
     * TODO: 连接关闭调用的方法
     *
     * @return
     * @throws
     * @author zhaoxi
     * @time 2018/12/4 16:36
     * @params
     */
    @OnClose
    public void onClose() {
        System.out.println("disconnect");
    }

    /**
     * TODO: 收到客户端消息后调用的方法
     *
     * @return
     * @throws
     * @author zhaoxi
     * @time 2018/12/4 16:36
     * @params
     */
    @OnMessage
    public void onMessage(Session session, String message) {
        System.out.println("来自客户端的消息:" + message);

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
        error.printStackTrace();
    }
}
