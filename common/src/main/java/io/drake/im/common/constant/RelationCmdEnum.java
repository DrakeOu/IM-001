package io.drake.im.common.constant;

import java.util.stream.Stream;

/**
 * Date: 2021/05/10/13:40
 *
 * @author : Drake
 * Description: cope with friend and group request
 */
public enum RelationCmdEnum {

    ACCEPT("接收请求"), DENY("拒绝请求"), BLOCK("拒绝并加入黑名单"), APPLY("申请");

    private String extend;

    RelationCmdEnum(String extend){
        this.extend = extend;
    }

    public static boolean checkValid(String cmd){
        return ACCEPT.name().equals(cmd) || DENY.name().equals(cmd) || BLOCK.name().equals(cmd) || APPLY.name().equals(cmd);
    }

    public static RelationCmdEnum getByName(String cmd){
        return Stream.of(values()).filter(type -> type.name().equals(cmd)).findFirst().orElseThrow(IllegalAccessError::new);
    }

}
