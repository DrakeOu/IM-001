package io.drake.im.common.codec;

import com.google.protobuf.Message;
import io.drake.im.protobuf.generate.IMAck;
import io.drake.im.protobuf.generate.IMInternal;
import io.drake.im.protobuf.generate.IMMessage;
import io.drake.im.protobuf.generate.constant.MsgTypeEnum;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MsgDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        in.markReaderIndex();

        if(in.readableBytes() < 8){
            return;
        }
        int code = in.readInt();
        int length = in.readInt();
        if(in.readableBytes() < length){
            in.resetReaderIndex();
            return;
        }

        byte[] bytes = new byte[length];
        in.readBytes(bytes);

        //根据类型进行不同的序列化
        Message message = null;
        MsgTypeEnum msgTypeEnum = MsgTypeEnum.getByCode(code);
        if(msgTypeEnum == MsgTypeEnum.MSG){
            message = IMMessage.ChatMsg.parseFrom(bytes);
        }
        if(msgTypeEnum == MsgTypeEnum.INTERNAL){
            message = IMInternal.Internal.parseFrom(bytes);
        }
        if(msgTypeEnum == MsgTypeEnum.ACK){
            message = IMAck.Ack.parseFrom(bytes);
        }

        out.add(message);

    }
}
