package io.drake.im.restweb.domain.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * Date: 2021/04/19/15:43
 *
 * @author : Drake
 * Description:
 */
@Data
@Entity(name = "users")
public class User {

    @Id
    @Column(name = "id")
    private String userId;

    @Column(name = "nick_name")
    private String nickName;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "friends")
    private String friends;

    @Column(name = "password")
    private String password;

    @Column(name = "create_time")
    private Date createTime;

    public User(){
        this.createTime = new Date();
    }
}
