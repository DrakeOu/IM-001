package io.drake.im.client.domain.conn;

import io.drake.im.common.domain.conn.AbstractConn;
import io.netty.channel.ChannelHandlerContext;

/**
 * Date: 2021/04/19/15:11
 *
 * @author : Drake
 * Description:
 */
public class ClientConn extends AbstractConn {

    public ClientConn(ChannelHandlerContext ctx) {
        super(ctx);
    }
}
