package io.drake.im.common.domain.http.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Date: 2021/04/20/22:03
 *
 * @author : Drake
 * Description:
 */
@Data
@NoArgsConstructor
public class UserReq {

    private String userName;
    private String password;
    private String userId;

    public UserReq(String userName, String password){
        this.userName = userName;
        this.password = password;
    }

    public UserReq(String userId){
        this.userId = userId;
    }

}
