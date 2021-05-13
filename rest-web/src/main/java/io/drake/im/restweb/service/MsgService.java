package io.drake.im.restweb.service;

import io.drake.im.common.domain.http.vo.OfflineMsgVO;
import io.drake.im.protobuf.generate.IMMessage;
import io.drake.im.restweb.domain.entity.User;

import java.util.List;

/**
 * Date: 2021/04/23/15:49
 *
 * @author : Drake
 * Description:
 */
public interface MsgService {

    Boolean saveMsg(IMMessage.ChatMsg msg);

    List<OfflineMsgVO> queryOffline(User user);
}
