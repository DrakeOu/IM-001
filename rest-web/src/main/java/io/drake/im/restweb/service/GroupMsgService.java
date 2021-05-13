package io.drake.im.restweb.service;

import io.drake.im.common.domain.http.vo.GroupOfflineMsgVO;
import io.drake.im.protobuf.generate.IMMessage;
import io.drake.im.restweb.domain.entity.GroupMember;
import io.drake.im.restweb.domain.entity.User;

import java.util.List;

/**
 * Date: 2021/05/11/20:43
 *
 * @author : Drake
 * Description:
 */
public interface GroupMsgService {

    void saveGroupMsg(IMMessage.ChatMsg groupMsg);

}
