package io.drake.im.restweb.controller;

import io.drake.im.common.domain.http.req.GroupReq;
import io.drake.im.common.domain.http.req.UserReq;
import io.drake.im.common.domain.http.vo.GroupOfflineMsgVO;
import io.drake.im.common.domain.http.vo.OfflineMsgVO;
import io.drake.im.common.domain.http.vo.RestResult;
import io.drake.im.restweb.domain.entity.GroupInfo;
import io.drake.im.restweb.domain.entity.GroupMsg;
import io.drake.im.restweb.domain.entity.User;
import io.drake.im.restweb.service.GroupService;
import io.drake.im.restweb.service.MsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 2021/05/03/21:01
 *
 * @author : Drake
 * Description:
 */
@RestController
@RequestMapping("/u")
public class MsgController {

    private final MsgService msgService;

    private final GroupService groupService;

    public MsgController(MsgService msgService, GroupService groupService) {
        this.msgService = msgService;
        this.groupService = groupService;
    }

    @RequestMapping("/pollOfflineMsg")
    public RestResult pollOfflineMsg(@RequestBody UserReq req){
        if(StringUtils.hasText(req.getUserId())){
            User user = new User();
            user.setUserId(req.getUserId());
            List<OfflineMsgVO> chatMsgs = msgService.queryOffline(user);
            return RestResult.success(chatMsgs);
        }
        return RestResult.fail("user not exist");
    }

    @RequestMapping("/pollGroupOffline")
    public RestResult pollGroupOffline(@RequestBody GroupReq req){
        Long groupId = req.getGroupId();
        String userId = req.getUserId();
        if(null == groupId || !StringUtils.hasText(userId)){
            return RestResult.fail("INVALID PARAMS");
        }
        GroupInfo group = groupService.findGroupById(groupId);
        List<GroupMsg> groupMsgs = groupService.pollOffline(userId, groupId);
        List<GroupOfflineMsgVO> offlines = new ArrayList<>();
        groupMsgs.forEach(m -> {
            GroupOfflineMsgVO msgVO = new GroupOfflineMsgVO();
            msgVO.setCreateTime(m.getCreateTime());
            msgVO.setGroupId(m.getGroupId());
            msgVO.setGroupName(group.getName());
            msgVO.setSenderId(m.getSenderId());
            msgVO.setSenderName(m.getSenderName());
            msgVO.setContent(m.getContent());
            offlines.add(msgVO);
        });

        return RestResult.success(offlines);
    }

}
