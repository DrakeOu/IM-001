package io.drake.im.restweb.service;

import io.drake.im.common.constant.RelationCmdEnum;
import io.drake.im.common.domain.http.vo.RelationVO;
import io.drake.im.restweb.domain.entity.User;
import io.drake.im.restweb.domain.entity.UserRelation;

import java.util.List;

/**
 * Date: 2021/04/19/16:23
 *
 * @author : Drake
 * Description:
 */
public interface UserService {

    User register(String userName, String password);

    User login(String userName, String password);

    List<RelationVO> friends(String userId);

    UserRelation addFriend(String userIdA, String userIdB);

    void updateFriendRelation(String userNameA, String userNameB, RelationCmdEnum cmd);

    User findByName(String userName);

    Boolean existUser(String userName);

    Boolean isFriend(String userIdA, String userIdB);
}
