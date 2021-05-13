package io.drake.im.restweb.service;

import io.drake.im.restweb.domain.entity.GroupInfo;
import io.drake.im.restweb.domain.entity.GroupMember;
import io.drake.im.restweb.domain.entity.GroupMsg;
import io.drake.im.restweb.domain.entity.User;

import java.util.List;

/**
 * Date: 2021/05/10/15:42
 *
 * @author : Drake
 * Description:
 */
public interface GroupService {

    GroupInfo createGroup(String groupName, User creator);

    GroupInfo findGroupById(Long id);

    List<GroupInfo> findAllGroupAsMember(User user);

    GroupInfo deleteGroup(String groupName);

    GroupMember inviteToGroup(String userName, User invitor, Long groupId);

    GroupMember kitFromGroup(String userName);

    Boolean isMember(String userId, Long groupId);

    List<GroupMsg> pollOffline(String userId, Long groupId);

    List<GroupMember> findAllGroupMember(Long groupId);
}
