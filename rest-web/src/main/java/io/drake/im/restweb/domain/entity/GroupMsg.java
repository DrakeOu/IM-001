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
@Entity(name = "group_msg")
public class GroupMsg {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "msg_offset")
    private Long msgOffset;

    @Column(name = "sender_id")
    private String senderId;

    @Column(name = "sender_name")
    private String senderName;

    @Column(name = "content")
    private String content;

    @Column(name = "create_time")
    private Date createTime;
}
