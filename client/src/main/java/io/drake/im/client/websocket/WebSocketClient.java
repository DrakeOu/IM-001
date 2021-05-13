package io.drake.im.client.websocket;

import io.drake.im.common.config.WebSocketConfiguration;
import io.drake.im.common.websocket.AbstractWebSocket;
import okhttp3.WebSocketListener;

/**
 * Date: 2021/05/09/14:29
 *
 * @author : Drake
 * Description:
 */
public class WebSocketClient extends AbstractWebSocket {

    public WebSocketClient(String url, WebSocketConfiguration configuration, WebSocketListener listener){
        super(url, configuration, listener);
    }

}
