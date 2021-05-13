package io.drake.im.common.domain.http.req;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Date: 2021/04/21/11:41
 *
 * @author : Drake
 * Description:
 */
@Data
@NoArgsConstructor
public class MsgReq {

    private String userName;
    private String msg;
}
