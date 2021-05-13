package io.drake.im.common.websocket;

import io.drake.im.common.config.WebSocketConfiguration;
import okhttp3.*;
import okio.ByteString;

/**
 * Date: 2021/05/09/14:09
 *
 * @author : Drake
 * Description:
 */
public abstract class AbstractWebSocket {

    private WebSocket ws;

    /**
     *
     * @param url
     * @param configuration
     * @param listener 没有设置填null
     */
    public AbstractWebSocket(String url, WebSocketConfiguration configuration, WebSocketListener listener){
        //todo configuration
        //open retry connect
        OkHttpClient httpClient = new OkHttpClient.Builder().retryOnConnectionFailure(true).build();
        Request request = new Request.Builder().url(url).build();

        httpClient.dispatcher().cancelAll();
        ws = httpClient.newWebSocket(request, null != listener ? listener : new DefaultListener());
    }

    public void close(){
        ws.close(1000, "client close ws");
    }

    protected static class DefaultListener extends WebSocketListener{
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            System.out.println("连接打开");
            webSocket.send("发送了一条数据");
            webSocket.send("{\"FID\":\"003\",\"SUB\":\"OFEX.BTCPERP.Depth\"}");
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            System.out.println("接收到消息：" + text);
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            System.out.println("这个可以不管，这个接收到是byte类型的");
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            System.out.println("连接关闭中");
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            System.out.println("连接关闭");
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            System.out.println("结束时，重连可以在这儿发起");
        }
    }

}
