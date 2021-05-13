package io.drake.im.common.domain.http.req;

import io.drake.im.common.constant.RelationCmdEnum;
import io.drake.im.common.domain.http.vo.UserInfoVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Date: 2021/05/07/22:44
 *
 * @author : Drake
 * Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelationReq {

    private String userA;
    private String userB;
    private String userNameA;
    private String userNameB;

    private RelationCmdEnum cmdType;

    public static RelationReq ofApplyFriend(UserInfoVO curUser, String userNameB){
        return new RelationReq(curUser.getUserId(), null, curUser.getUserName(), userNameB, RelationCmdEnum.APPLY);
    }

    public static RelationReq ofUpdateFriend(UserInfoVO curUser, String userNameA, String cmd){
        return new RelationReq(null, curUser.getUserId(), userNameA, curUser.getUserName(), RelationCmdEnum.getByName(cmd));
    }
}
