package io.drake.im.connector.handler;

import com.google.protobuf.Message;
import io.drake.im.common.domain.conn.Conn;
import io.drake.im.common.function.AbstractParser;
import io.drake.im.connector.service.ConnectorClientService;
import io.drake.im.protobuf.generate.IMAck;
import io.drake.im.protobuf.generate.IMInternal;
import io.drake.im.protobuf.generate.IMMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Date: 2021/04/21/11:51
 *
 * @author : Drake
 * Description:
 */
@ChannelHandler.Sharable
public class ConnectorToClientHandler extends SimpleChannelInboundHandler<Message> {

    private static final Logger log = LoggerFactory.getLogger(ConnectorToClientHandler.class);

    private ConnectorClientService connectorClientService;
    private AbstractParser parser;

    public ConnectorToClientHandler(ConnectorClientService connectorClientService){
        this.connectorClientService = connectorClientService;
        this.parser = new ConnectorClientParser();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        if(null == msg){
            return;
        }
        parser.parseMsg(msg, ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.debug("CONNID {} IS OFFLINE", ctx.channel().attr(Conn.CONN_ID));
    }

    private class ConnectorClientParser extends AbstractParser{

        @Override
        protected void registerParser() {

            register(IMMessage.ChatMsg.class, (m, ctx) -> connectorClientService.sendMsg(m));

            register(IMInternal.Internal.class, (m, ctx) -> connectorClientService.handleInternal(m, ctx));

            register(IMAck.Ack.class, (m, ctx) -> connectorClientService.handleAck(m, ctx));
        }
    }

}
