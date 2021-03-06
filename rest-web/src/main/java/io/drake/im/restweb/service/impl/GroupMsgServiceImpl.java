package io.drake.im.restweb.service.impl;

import io.drake.im.common.domain.http.vo.GroupOfflineMsgVO;
import io.drake.im.protobuf.generate.IMMessage;
import io.drake.im.restweb.domain.entity.GroupInfo;
import io.drake.im.restweb.domain.entity.GroupMember;
import io.drake.im.restweb.domain.entity.GroupMsg;
import io.drake.im.restweb.domain.entity.GroupReadOffset;
import io.drake.im.restweb.repository.GroupMsgRepository;
import io.drake.im.restweb.repository.GroupReadOffsetRepository;
import io.drake.im.restweb.service.GroupMsgService;
import io.drake.im.restweb.service.GroupService;
import io.drake.im.restweb.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Date: 2021/05/11/20:44
 *
 * @author : Drake
 * Description:
 */
@Service
@Slf4j
public class GroupMsgServiceImpl implements GroupMsgService {

    private final GroupMsgRepository groupMsgRepository;

    private final GroupReadOffsetRepository groupReadOffsetRepository;

    private final GroupService groupService;

    private final UserService userService;

    private static final Long FIRST = 1L;

    public GroupMsgServiceImpl(GroupMsgRepository groupMsgRepository, GroupService groupService, GroupReadOffsetRepository groupReadOffsetRepository, UserService userService) {
        this.groupMsgRepository = groupMsgRepository;
        this.groupService = groupService;
        this.groupReadOffsetRepository = groupReadOffsetRepository;
        this.userService = userService;
    }

    @Override
    @Transactional
    public void saveGroupMsg(IMMessage.ChatMsg groupMsg) {
        GroupMsg msg = new GroupMsg();
        Long groupId = Long.valueOf(groupMsg.getDestId());
        msg.setContent(groupMsg.getContent());
        msg.setCreateTime(new Date());
        msg.setGroupId(groupId);
        msg.setSenderId(groupMsg.getOriginId());
        msg.setSenderName(groupMsg.getOriginName());
        msg.setMsgOffset(nextOffset(groupId));
        GroupMsg latest = groupMsgRepository.save(msg);
        //todo read offset

        List<GroupMember> members = groupService.findAllGroupMember(latest.getGroupId());
        updateOffsetForMember(latest, members, groupMsg.getGroupMembersList());
    }

    public GroupMsg findLatestMsgByGroup(Long groupId){
        return groupMsgRepository.findFirstByGroupIdOrderByMsgOffsetDesc(groupId);
    }


    //??????????????????????????????????????????offset??????????????????????????????????????????????????????
    private void updateOffsetForMember(GroupMsg latest, List<GroupMember> members, List<String> notOnlineMembers){
        Set<String> offlineMembers = new HashSet<>(notOnlineMembers);
        for(GroupMember member: members){
            if(offlineMembers.contains(member.getUserId())){
                doUpdateOffsetForOffline(latest, member);
            }else{
                doUpdateOffsetForOnline(latest, member);
            }
        }
    }

    /**
     * todo ???????????????????????????
     * 1. ????????????????????????????????????A
     * 2. ?????????????????????????????????B????????????????????????B
     * 3. A???B???????????????offset????????????
     * 4. ?????????A???????????????????????????????????????????????????????????????
     * 5. ????????????????????????A???B????????????????????????offset???????????????A???????????????????????????????????????
     */
    private void doUpdateOffsetForOnline(GroupMsg latest, GroupMember member) {
        GroupReadOffset offset = groupReadOffsetRepository.findByGroupIdAndUserId(member.getGroupId(), member.getUserId());
        if(null == offset){
            offset = GroupReadOffset.of(member, latest.getMsgOffset());
        }
        if(checkOffsetJump(latest, offset)){
            log.error("FATAL ERROR, OFFSET OF GROUP [{}] MEMBER [{}] JUMP FROM {} TO {}",
                    member.getGroupId(), member.getUserName(), offset.getReadOffset(), latest.getMsgOffset());
        }
        offset.setReadOffset(latest.getMsgOffset());
        groupReadOffsetRepository.save(offset);
    }

    //todo ???????????????????????????????????????????????????ws?????????????????????
    private void doUpdateOffsetForOffline(GroupMsg latest, GroupMember member) {
        GroupReadOffset offset = groupReadOffsetRepository.findByGroupIdAndUserId(member.getGroupId(), member.getUserId());
        if(null == offset){
            offset = GroupReadOffset.of(member, latest.getMsgOffset()-1);
            groupReadOffsetRepository.save(offset);
        }
    }

    private boolean checkOffsetJump(GroupMsg groupMsg, GroupReadOffset offset){
        return groupMsg.getMsgOffset() - offset.getReadOffset() > 1;
    }


    private Long nextOffset(Long groupId){
        GroupMsg latest = findLatestMsgByGroup(groupId);
        if(latest == null){
            return FIRST;
        }else{
            return latest.getMsgOffset() + 1;
        }
    }
}
