package io.drake.im.common.codec.deprecated;

/**
 * Date: 2021/05/03/18:40
 *
 * @author : Drake
 * Description:
 */
public class Greet extends Msg{
    @Override
    public MsgType getType() {
        return MsgType.GREET;
    }
}
