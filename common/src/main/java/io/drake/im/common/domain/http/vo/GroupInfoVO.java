package io.drake.im.common.domain.http.vo;

import lombok.Data;

import java.util.Date;

/**
 * Date: 2021/05/11/12:12
 *
 * @author : Drake
 * Description:
 */
@Data
public class GroupInfoVO {

    private Long id;

    private String name;

    private String ownerId;

    private String ownerName;

    private Integer memberCount;

    private String briefInfo;

    private Date createTime;
}
