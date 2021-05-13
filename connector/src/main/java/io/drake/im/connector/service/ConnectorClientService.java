package io.drake.im.connector.service;

import com.google.protobuf.Message;
import com.google.protobuf.ProtocolStringList;
import io.drake.im.common.domain.conn.MemoryConnContext;
import io.drake.im.common.service.SessionService;
import io.drake.im.connector.TransferClient;
import io.drake.im.connector.domain.conn.ConnectorConn;
import io.drake.im.connector.handler.ConnectorToTransferHandler;
import io.drake.im.protobuf.generate.IMAck;
import io.drake.im.protobuf.generate.IMInternal;
import io.drake.im.protobuf.generate.IMMessage;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 2021/04/21/12:06
 *
 * @author : Drake
 * Description: should be thread safe
 */
@Slf4j
public class ConnectorClientService {

    private MemoryConnContext<ConnectorConn> connContext;
    private SessionService sessionService;

    public ConnectorClientService(MemoryConnContext<ConnectorConn> connContext, SessionService sessionService, TransferClient transferClient){
        this.connContext = connContext;
        this.sessionService = sessionService;
    }


    public void registerLogin(String userId, ChannelHandlerContext ctx){
        //redis 没有设置过期策略，导致重启后仍有session数据但内存context中没有
        if(sessionService.isOnline(userId) && null != connContext.getConn(userId)){
            return;
        }
        ConnectorConn conn = new ConnectorConn(ctx);
        connContext.add(conn, userId);
        sessionService.online(userId, conn.getConnId().toString());
        log.debug("user[{}] login with connId[{}]", userId, conn.getConnId());
    }

    public void sendMsg(IMMessage.ChatMsg msg){
        //区分single和group
        if(msg.getType() == IMMessage.ChatMsg.MessageType.SINGLE){
            sendSingleMsg(msg);
        }else if(msg.getType() == IMMessage.ChatMsg.MessageType.GROUP){
            sendGroupMsg(msg);
        }

    }

    private void sendSingleMsg(IMMessage.ChatMsg msg){
        String toUser = msg.getDestId();

        if(!sessionService.isOnline(toUser)){
            // user if offline then send to transfer
            IMAck.Ack ack = IMAck.Ack.newBuilder()
                    .setExtend(String.format("USER %s IS NOT ONLINE!", toUser))
                    .setAckType(IMAck.Ack.AckType.DELIVERED)
                    .setOriginId(msg.getOriginId())
                    .setDestId(msg.getDestId())
                    .setDestType(IMAck.Ack.DestType.SINGLE)
                    .build();
            ConnectorConn conn = connContext.getConn(msg.getOriginId());
            conn.getCtx().channel().writeAndFlush(ack);

        }else{

            ConnectorConn conn = connContext.getConn(toUser);
            IMMessage.ChatMsg signedMsg = IMMessage.ChatMsg.newBuilder().mergeFrom(msg).setIsSigned(true).build();
            conn.getCtx().channel().writeAndFlush(signedMsg);
        }

        sendToTransfer(msg);

    }

    /**
     * 需要根据先消费，后同步进行设计
     * 这里记录不在线的群员传递下去，最后更新offset时，期间加入的群员也按已读更新（虽然他们不会真的收到消息，但是需要确保拉取离线消息时不会拉到入群前的消息）
     * 消息发送只由发送时的群员决定最终收到的人员
     * @param msg
     */
    private void sendGroupMsg(IMMessage.ChatMsg msg){
        List<String> groupMembersList = msg.getGroupMembersList();
        List<String> notOnlineMembers = new ArrayList<>();
        for(String memberId: groupMembersList){
            ConnectorConn conn = connContext.getConn(memberId);
            //根据在线情况进行实时发送，同时都将进行持久化
            if(sessionService.isOnline(memberId) && null != conn){
                if(memberId.equals(msg.getOriginId())) continue;
                conn.getCtx().channel().writeAndFlush(msg);

            }else{
                if(null == conn){
                    //处理因为redis导致的假在线情况
                    sessionService.offline(memberId);
                }
                notOnlineMembers.add(memberId);
            }

        }
        //send to transfer
        //todo 可能考虑批处理？
        sendToTransfer(IMMessage.ChatMsg.newBuilder().mergeFrom(msg).clearGroupMembers().addAllGroupMembers(notOnlineMembers).build());
        //todo ack

    }


    public void handleInternal(IMInternal.Internal internal, ChannelHandlerContext ctx) {
        //区分ack,error,greet
        if(internal.getMsgType() == IMInternal.Internal.MsgType.GREET){
            String userId = internal.getBody();
            log.debug("GREET FROM CLIENT {}", userId);
            if(connContext.getConn(userId) == null){
                ConnectorConn conn = new ConnectorConn(ctx);
                connContext.add(conn, userId);
                sessionService.online(userId, conn.getConnId().toString());
                log.debug("USER {} IS NOW ONLINE WITH CONNID {}", userId, conn.getConnId().toString());
            }else{
                log.debug("REDUNDANT GREET FROM CLIENT {}, DROP", userId);
            }
        }
        if(internal.getMsgType() == IMInternal.Internal.MsgType.ERROR){

        }
        if(internal.getMsgType() == IMInternal.Internal.MsgType.ACK){

        }
    }

    public void handleAck(IMAck.Ack ack, ChannelHandlerContext ctx) {
    }

    private void sendToTransfer(Message msg){
        ChannelHandlerContext transferCtx = ConnectorToTransferHandler.getOneTransferCtx();
        if(null == transferCtx){
            //todo reconnect to transfer
            log.error("loss all connect to transfer");
        }else{
            transferCtx.channel().writeAndFlush(msg);
        }
    }
}
