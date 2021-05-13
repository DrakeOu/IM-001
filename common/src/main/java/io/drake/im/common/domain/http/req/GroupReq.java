package io.drake.im.common.domain.http.req;

import io.drake.im.common.domain.http.vo.UserInfoVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Date: 2021/05/10/20:22
 *
 * @author : Drake
 * Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupReq {

    private String userId;
    private String userName;
    private String groupName;
    private Long groupId;
    //userName
    private String invitedUser;
    private String kickedUser;

    public static GroupReq ofNewGroup(UserInfoVO curUser, String groupName){
        return new GroupReq(curUser.getUserId(), curUser.getUserName(), groupName, null, null, null);
    }

    public static GroupReq ofInviteUser(UserInfoVO curUser, String userName, Long groupId){
        return new GroupReq(curUser.getUserId(), curUser.getUserName(), null, groupId, userName, null);
    }

    public static GroupReq ofGroupMember(UserInfoVO curUser, Long groupId){
        return new GroupReq(curUser.getUserId(), curUser.getUserName(), null, groupId, null, null);
    }

}
