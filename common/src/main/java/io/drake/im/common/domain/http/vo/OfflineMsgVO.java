package io.drake.im.common.domain.http.vo;

import lombok.Data;

import java.util.Date;

/**
 * Date: 2021/05/03/20:41
 *
 * @author : Drake
 * Description:
 */
@Data
public class OfflineMsgVO {

    private String origin;
    private String dest;
    private String content;
    private Date createAt;

}
