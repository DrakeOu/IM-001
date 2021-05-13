package io.drake.im.common.codec.deprecated;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.NoArgsConstructor;

/**
 * Date: 2021/04/21/15:05
 *
 * @author : Drake
 * Description:
 */
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Ack extends Msg{

    public Ack(String userName){
        this.fromId = userName;
    }

    @Override
    public MsgType getType() {
        return MsgType.ACK;
    }


}
