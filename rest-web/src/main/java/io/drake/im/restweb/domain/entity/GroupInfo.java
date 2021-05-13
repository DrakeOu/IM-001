package io.drake.im.restweb.domain.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Date: 2021/05/10/15:09
 *
 * @author : Drake
 * Description:
 */
@Entity(name = "group_info")
@Data
public class GroupInfo {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "owner_id")
    private String ownerId;

    @Column(name = "owner_name")
    private String ownerName;

    @Column(name = "member_count")
    private Integer memberCount;

    @Column(name = "brief_info")
    private String briefInfo;

    @Column(name = "create_time")
    private Date createTime;
}
