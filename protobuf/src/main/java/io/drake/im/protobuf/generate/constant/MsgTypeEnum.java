package io.drake.im.protobuf.generate.constant;

import io.drake.im.protobuf.generate.IMAck;
import io.drake.im.protobuf.generate.IMInternal;
import io.drake.im.protobuf.generate.IMMessage;

import java.util.stream.Stream;

/**
 * Date: 2021/05/06/15:24
 *
 * @author : Drake
 * Description:
 */
public enum MsgTypeEnum {

    MSG(0, IMMessage.ChatMsg.class), INTERNAL(1, IMInternal.Internal.class), ACK(2, IMAck.Ack.class);

    int code;
    Class<?> clazz;

    MsgTypeEnum(int code, Class<?> clazz){
        this.code = code;
        this.clazz = clazz;
    }

    public int getCode(){
        return code;
    }

    public static MsgTypeEnum getByCode(int code){
        return Stream.of(values()).filter(t -> t.code == code)
                .findFirst().orElseThrow(IllegalArgumentException::new);
    }

    public static MsgTypeEnum getByClass(Class<?> clazz){
        return Stream.of(values()).filter(t -> t.clazz == clazz)
                .findFirst().orElseThrow(IllegalArgumentException::new);
    }
}
