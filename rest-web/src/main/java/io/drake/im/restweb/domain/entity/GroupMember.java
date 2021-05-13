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
@Data
@Entity(name = "group_member")
public class GroupMember {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "username")
    private String userName;

    @Column(name = "level")
    private Integer level;

    @Column(name = "join_time")
    private Date joinTime;
}
