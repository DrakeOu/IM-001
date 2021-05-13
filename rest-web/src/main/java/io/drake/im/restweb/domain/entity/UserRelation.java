package io.drake.im.restweb.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * Date: 2021/05/07/19:52
 *
 * @author : Drake
 * Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "user_relation")
public class UserRelation {

    @Id
    @Column(name = "user_a")
    private String userA;

    @Column(name = "user_b")
    private String userB;

    @Column(name = "user_name_a")
    private String userNameA;

    @Column(name = "user_name_b")
    private String userNameB;

    @Column(name = "status")
    private String status;

    @Column(name = "create_time")
    private Date createTime;

}
