package io.drake.im.common.domain.http.vo;

import lombok.Data;

import java.util.Date;

/**
 * Date: 2021/05/13/11:31
 *
 * @author : Drake
 * Description:
 */
@Data
public class GroupOfflineMsgVO {

    private String senderId;
    private String senderName;
    private String groupName;
    private String content;
    private Long groupId;
    private Date createTime;


}
