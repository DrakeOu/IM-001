package io.drake.im.connector.domain.conn;

import io.drake.im.common.domain.conn.AbstractConn;
import io.netty.channel.ChannelHandlerContext;

/**
 * Date: 2021/04/19/15:12
 *
 * @author : Drake
 * Description:
 */
public class ConnectorConn extends AbstractConn {

    public ConnectorConn(ChannelHandlerContext ctx) {
        super(ctx);
    }
}
