package io.drake.im.restweb.constant;

/**
 * Date: 2021/05/07/20:48
 *
 * @author : Drake
 * Description:
 */
public enum UserRelationEnum {

    DONE("已是好友"), DENY("已拒绝"), BLOCK("已被加入黑名单"), TODO("待处理"), ACCEPT("已接受");

    private final String extend;

    UserRelationEnum(String extend){
        this.extend = extend;
    }

}
