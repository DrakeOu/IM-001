package io.drake.im.client.websocket;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

import javax.annotation.Nullable;

/**
 * Date: 2021/05/09/14:55
 *
 * @author : Drake
 * Description:
 */
@Slf4j
public class ClientWSListener extends WebSocketListener {

    //处理消息
    @Override
    public void onMessage(WebSocket webSocket, String text) {
        log.debug("WS:RECV TEXT {}", text);
    }

    //处理重连
    @Override
    public void onFailure(WebSocket webSocket, Throwable t, @Nullable Response response) {
        super.onFailure(webSocket, t, response);
    }
}
