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
@Entity(name = "group_read_offset")
public class GroupReadOffset {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "read_offset")
    private Long readOffset;

    @Column(name = "recent_read")
    private Date recentRead;

    public static GroupReadOffset of(GroupMember member, Long readOffset){
        GroupReadOffset offset = new GroupReadOffset();
        offset.setRecentRead(new Date());
        offset.setGroupId(member.getGroupId());
        offset.setUserId(member.getUserId());
        offset.setReadOffset(readOffset);
        return offset;
    }
}
