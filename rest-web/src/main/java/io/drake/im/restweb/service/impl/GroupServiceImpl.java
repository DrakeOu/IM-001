package io.drake.im.restweb.service.impl;

import io.drake.im.restweb.domain.entity.*;
import io.drake.im.restweb.repository.*;
import io.drake.im.restweb.service.GroupService;
import io.drake.im.restweb.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Date: 2021/05/10/16:28
 *
 * @author : Drake
 * Description:
 */
@Service
@Slf4j
public class GroupServiceImpl implements GroupService {


    private final GroupInfoRepository groupInfoRepository;

    private final GroupMemberRepository groupMemberRepository;

    private final GroupMsgRepository groupMsgRepository;

    private final GroupReadOffsetRepository groupReadOffsetRepository;

    private final UserRelationRepository userRelationRepository;

    private final UserRepository userRepository;

    private final UserService userService;

    public GroupServiceImpl(GroupInfoRepository groupInfoRepository, GroupMemberRepository groupMemberRepository, GroupMsgRepository groupMsgRepository, GroupReadOffsetRepository groupReadOffsetRepository, UserRelationRepository userRelationRepository, UserRepository userRepository, UserService userService) {
        this.groupInfoRepository = groupInfoRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.groupMsgRepository = groupMsgRepository;
        this.groupReadOffsetRepository = groupReadOffsetRepository;
        this.userRelationRepository = userRelationRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    public GroupInfo createGroup(String groupName, User creator) {
        GroupInfo groupInfo = new GroupInfo();
        groupInfo.setCreateTime(new Date());
        groupInfo.setName(groupName);
        groupInfo.setOwnerId(creator.getUserId());
        groupInfo.setOwnerName(creator.getUserName());
        groupInfo.setMemberCount(0);
        GroupInfo group = groupInfoRepository.save(groupInfo);

        GroupMember groupMember = new GroupMember();
        groupMember.setUserName(creator.getUserName());
        groupMember.setUserId(creator.getUserId());
        groupMember.setJoinTime(new Date());
        groupMember.setGroupId(group.getId());
        groupMemberRepository.save(groupMember);
        return groupInfo;
    }

    @Override
    public GroupInfo findGroupById(Long id) {
        return groupInfoRepository.findById(id).orElse(null);
    }

    @Override
    public List<GroupInfo> findAllGroupAsMember(User user) {
        List<GroupMember> asMembers = groupMemberRepository.findByUserId(user.getUserId());
        List<GroupInfo> infos = new ArrayList<>();
        asMembers.forEach(m -> infos.add(groupInfoRepository.findById(m.getGroupId()).orElse(new GroupInfo())));
        return infos;
    }

    @Override
    public GroupInfo deleteGroup(String groupName) {
        return null;
    }

    @Override
    public GroupMember inviteToGroup(String userName, User invitor, Long groupId) {
        User invited = userService.findByName(userName);
        if(userService.isFriend(invited.getUserId(), invitor.getUserId()) && !isMember(invited.getUserId(), groupId)){
            GroupMember newMember = new GroupMember();
            newMember.setGroupId(groupId);
            newMember.setJoinTime(new Date());
            newMember.setUserId(invited.getUserId());
            newMember.setUserName(invited.getUserName());
            groupMemberRepository.save(newMember);
            return newMember;
        }

        return null;
    }

    @Override
    public GroupMember kitFromGroup(String userName) {
        return null;
    }

    @Override
    public Boolean isMember(String userId, Long groupId) {
        return null != groupMemberRepository.findByGroupIdAndUserId(groupId, userId);
    }

    @Override
    @Transactional
    public List<GroupMsg> pollOffline(String userId, Long groupId) {
        if(!isMember(userId, groupId)){
            return new ArrayList<>();
        }
        GroupReadOffset offset = groupReadOffsetRepository.findByGroupIdAndUserId(groupId, userId);

        List<GroupMsg> offlines = groupMsgRepository.findByGroupIdAndMsgOffsetAfter(groupId, offset.getReadOffset());
        Long curOffset = offset.getReadOffset();
        for(GroupMsg msg: offlines) curOffset = Math.max(curOffset, msg.getMsgOffset());

        offset.setReadOffset(curOffset);
        offset.setRecentRead(new Date());
        return offlines;
    }

    @Override
    public List<GroupMember> findAllGroupMember(Long groupId) {
        return groupMemberRepository.findByGroupId(groupId);
    }
}
