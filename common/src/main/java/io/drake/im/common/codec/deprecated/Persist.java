package io.drake.im.common.codec.deprecated;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Date: 2021/04/22/12:12
 *
 * @author : Drake
 * Description:
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Persist extends Msg {

    private String to;

    @Override
    public MsgType getType() {
        return MsgType.PERSIST;
    }
}
