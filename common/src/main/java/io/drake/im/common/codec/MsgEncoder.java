package io.drake.im.common.codec;

import com.google.protobuf.Message;
import io.drake.im.protobuf.generate.constant.MsgTypeEnum;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MsgEncoder extends MessageToByteEncoder<Message> {

    //这里入参的byteBuf是哪里来的
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message msg, ByteBuf out) throws Exception {

        byte[] bytes = msg.toByteArray();
        int code = MsgTypeEnum.getByClass(msg.getClass()).getCode();
        int length = bytes.length;

        out.writeInt(code);
        out.writeInt(length);
        out.writeBytes(bytes);

    }
}
