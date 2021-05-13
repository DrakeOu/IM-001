package io.drake.im.common.websocket;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

import javax.annotation.Nullable;

/**
 * Date: 2021/05/09/14:56
 *
 * @author : Drake
 * Description:
 */
public abstract class AbstractWSListener extends WebSocketListener {

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        super.onMessage(webSocket, text);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, @Nullable Response response) {
        super.onFailure(webSocket, t, response);
    }
}
