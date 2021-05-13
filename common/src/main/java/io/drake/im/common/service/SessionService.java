package io.drake.im.common.service;

/**
 * Date: 2021/04/21/10:16
 *
 * @author : Drake
 * Description:
 */
public interface SessionService {

    String SESSION_KEY = "USER_SESSION:USERID-CONNID";

    String online(String userId, String connId);

    String offline(String userId);

    Boolean isOnline(String userId);

}
