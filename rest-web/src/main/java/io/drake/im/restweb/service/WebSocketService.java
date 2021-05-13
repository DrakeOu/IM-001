package io.drake.im.restweb.service;

import io.drake.im.restweb.domain.ws.WSEvent;
import org.springframework.stereotype.Component;

/**
 * Date: 2021/05/09/15:30
 *
 * @author : Drake
 * Description:
 */
public interface WebSocketService {

    void sendEvent(String userId, WSEvent event);

    void sendGroupEvent(String groupId, WSEvent event);
}
