package io.drake.im.restweb.domain.ws;

import io.drake.im.restweb.domain.entity.User;
import lombok.Data;

/**
 * Date: 2021/05/09/15:43
 *
 * @author : Drake
 * Description: 用来通知好友和群组管理消息，目前不涉及负责功能，假设仅传递str给客户端展示
 */
@Data
public class WSEvent {

    private String id;
    private String name;
    private String content;
    private EventType eventType;

    enum EventType{
        FRIEND, GROUP
    }

    public static WSEvent ofFriend(User user, String content){
        WSEvent event = new WSEvent();
        event.setEventType(EventType.FRIEND);
        event.setId(user.getUserId());
        event.setName(user.getUserName());
        event.setContent(content);
        return event;
    }

    public static WSEvent ofGroup(){
        return null;
    }

}
