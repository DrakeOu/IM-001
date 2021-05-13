package io.drake.im.common.codec.deprecated;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SingleMsg extends Msg{

    private String toId;
    private Boolean isSigned;

    public SingleMsg(String fromId, String toId, String msg){
        this.fromId = fromId;
        this.toId = toId;
        this.chatMsg = msg;
        this.isSigned = false;
    }

    @Override
    public MsgType getType() {
        return MsgType.SINGLE;
    }
}
