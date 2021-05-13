package io.drake.im.common.domain.http.vo;

import lombok.Data;

import java.util.Date;

/**
 * Date: 2021/05/11/12:15
 *
 * @author : Drake
 * Description:
 */
@Data
public class GroupMemberVO {

    private Long id;

    private Long groupId;

    private String userId;

    private String userName;

    private Integer level;

    private Date joinTime;
}
