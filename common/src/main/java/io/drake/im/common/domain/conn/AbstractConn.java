package io.drake.im.common.domain.conn;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Date: 2021/04/19/14:23
 *
 * @author: Drake
 * Description:
 */
public abstract class AbstractConn implements Conn{

    private static final AtomicInteger ShareId = new AtomicInteger(0);

    private int ConnId;
    private ChannelHandlerContext ctx;

    public AbstractConn(ChannelHandlerContext ctx){
        this.ctx = ctx;
        ConnId = ShareId.incrementAndGet();
        ctx.channel().attr(CONN_ID).set(ConnId);
    }

    @Override
    public Serializable getConnId() {
        return ConnId;
    }

    @Override
    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    @Override
    public ChannelFuture close() {
        return ctx.close();
    }
}
