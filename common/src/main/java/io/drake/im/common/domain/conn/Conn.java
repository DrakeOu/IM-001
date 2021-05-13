package io.drake.im.common.domain.conn;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

import java.io.Serializable;

/**
 * date : 2021/04/19/14:16
 * @author : Drake
 * description: 连接
 */
public interface Conn {

    AttributeKey<Serializable> CONN_ID = AttributeKey.valueOf("CONN_ID");

    Serializable getConnId();

    ChannelHandlerContext getCtx();

    ChannelFuture close();

}
