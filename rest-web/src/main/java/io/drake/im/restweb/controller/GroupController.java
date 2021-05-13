package io.drake.im.restweb.controller;

import io.drake.im.common.domain.http.req.GroupReq;
import io.drake.im.common.domain.http.req.UserReq;
import io.drake.im.common.domain.http.vo.RestResult;
import io.drake.im.restweb.domain.entity.GroupInfo;
import io.drake.im.restweb.domain.entity.GroupMember;
import io.drake.im.restweb.domain.entity.User;
import io.drake.im.restweb.service.GroupService;
import io.drake.im.restweb.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * Date: 2021/05/10/19:56
 *
 * @author : Drake
 * Description:
 */
@RestController
@Slf4j
@RequestMapping("/u")
public class GroupController {

    private final GroupService groupService;
    private final UserService userService;

    public GroupController(GroupService groupService, UserService userService) {
        this.groupService = groupService;
        this.userService = userService;
    }

    @RequestMapping("/createGroup")
    public RestResult createGroup(@RequestBody GroupReq req){
        User creator = userService.findByName(req.getUserName());
        GroupInfo groupInfo = groupService.createGroup(req.getGroupName(), creator);
        if(null == groupInfo){
            return RestResult.fail(String.format("GROUP CREATE FAIL, GROUP [%s] ALREADY EXIST", req.getGroupName()));
        }else{
            return RestResult.success(groupInfo);
        }
    }

    @RequestMapping("/inviteUser")
    public RestResult inviteUser(@RequestBody GroupReq req){
        Long groupId = req.getGroupId();
        String invitedUser = req.getInvitedUser();
        String ownerId = req.getUserId();
        if(!userService.existUser(req.getInvitedUser())){
            return RestResult.fail(String.format("INVITED USER %s NOT EXIST", invitedUser));
        }
        if(groupService.findGroupById(groupId) == null){
            return RestResult.fail(String.format("GROUP [%s] IS NOT EXIST", groupId));
        }
        User invited = userService.findByName(invitedUser);
        if(!groupService.isMember(ownerId, groupId) || groupService.isMember(invited.getUserId(), groupId)){
            return RestResult.fail("YOU ARE NOT MEMBER OF TARGET GROUP, OR INVITED IS ALREADY MEMBER");
        }

        GroupMember member = groupService.inviteToGroup(invitedUser, userService.findByName(req.getUserName()), groupId);
        return RestResult.success(member);
    }

    @RequestMapping("/kickUser")
    public RestResult kickUser(@RequestBody GroupReq req){
        return RestResult.fail("NOT READY YET");
    }

    @RequestMapping("/dismissGroup")
    public RestResult dismissGroup(@RequestBody GroupReq req){
        return RestResult.fail("NOT READY YET");
    }

    @RequestMapping("/groups")
    public RestResult queryGroups(@RequestBody UserReq req){
        String userId = req.getUserId();
        String userName = req.getUserName();
        if(!StringUtils.hasText(userId) && !StringUtils.hasText(userName)){
            return RestResult.fail("INVALID PARAM, USERID AND USERNAME ARE EMPTY");
        }
        List<GroupInfo> groupInfos = groupService.findAllGroupAsMember(userService.findByName(userName));
        return RestResult.success(groupInfos);
    }

    @RequestMapping("/groupMembers")
    public RestResult groupMembers(@RequestBody GroupReq req){
        String userId = req.getUserId();
        Long groupId = req.getGroupId();
        if(null == groupId || null == groupService.findGroupById(groupId)){
            return RestResult.fail(String.format("GROUP [%s] NOT EXIST", groupId));
        }
        List<GroupMember> members = groupService.findAllGroupMember(groupId);
        return RestResult.success(members);
    }

}
