package io.drake.im.common.domain.http.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * Date: 2021/04/19/21:17
 *
 * @author : Drake
 * Description:
 */
@Data
@NoArgsConstructor
public class UserInfoVO {

    private String userId;
    private String userName;
    private String nickName;
    private List<RelationVO> friends;
    private String token;
    private InetSocketAddress nettyAddress;
    private String wsUrl;

    public UserInfoVO(String userId, String userName, String nickName){
        this.userId = userId;
        this.userName = userName;
        this.nickName = nickName;
    }

}
