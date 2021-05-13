package io.drake.im.restweb.service.impl;

import io.drake.im.common.domain.http.vo.OfflineMsgVO;
import io.drake.im.protobuf.generate.IMMessage;
import io.drake.im.restweb.domain.entity.ChatMsg;
import io.drake.im.restweb.domain.entity.User;
import io.drake.im.restweb.repository.ChatMsgRepository;
import io.drake.im.restweb.service.MsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Date: 2021/04/23/15:50
 *
 * @author : Drake
 * Description:
 */
@Service
public class MsgServiceImpl implements MsgService {

    @Autowired
    private ChatMsgRepository repository;

    @Override
    public Boolean saveMsg(IMMessage.ChatMsg msg) {
        ChatMsg chatMsg = new ChatMsg();
        chatMsg.setContent(msg.getContent());
        chatMsg.setOrigin(msg.getOriginId());
        chatMsg.setDest(msg.getDestId());
        chatMsg.setIsSigned(msg.getIsSigned());
        chatMsg.setCreateTime(new Date(msg.getCreateTime()));
        repository.save(chatMsg);

        return true;
    }

    @Override
    public List<OfflineMsgVO> queryOffline(User user) {
        if(null == user){
            return null;
        }
        List<ChatMsg> chatMsgs = repository.findByDestAndIsSigned(user.getUserId(), false);
        List<OfflineMsgVO> offlineMsgs = new ArrayList<>();
        chatMsgs.forEach(chatMsg -> {
            OfflineMsgVO msg = new OfflineMsgVO();
            msg.setDest(chatMsg.getDest());
            msg.setOrigin(chatMsg.getOrigin());
            msg.setContent(chatMsg.getContent());
            msg.setCreateAt(chatMsg.getCreateTime());
            offlineMsgs.add(msg);
            chatMsg.setIsSigned(true);
            repository.save(chatMsg);
        });
        return offlineMsgs;
    }
}
