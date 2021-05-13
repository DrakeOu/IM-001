package io.drake.im.client.api;

import io.drake.im.common.domain.http.req.GroupReq;
import io.drake.im.common.domain.http.req.RelationReq;
import io.drake.im.common.domain.http.req.UserReq;
import io.drake.im.common.domain.http.vo.*;
import io.drake.im.common.rest.AbstractRestService;

import java.util.List;

/**
 * Date: 2021/04/20/20:53
 *
 * @author : Drake
 * Description: result may be null
 */
public class RestClientService extends AbstractRestService<ClientRestClient> {

    public RestClientService(Class<ClientRestClient> clazz, String url) {
        super(clazz, url);
    }

    public UserInfoVO login(String userName, String password){
        return doRequest(() -> restClient.login(new UserReq(userName, password)).execute());
    }

    public UserInfoVO register(String userName, String password){
        return doRequest(() -> restClient.register(new UserReq(userName, password)).execute());
    }

    public UserInfoVO friends(String userId){
        return doRequest(() -> restClient.friends(new UserReq(userId)).execute());
    }

    public List<OfflineMsgVO> pollOffline(String userId){
        return doRequest(() -> restClient.pollOfflineMsg(new UserReq(userId)).execute());
    }

    public String addFriend(RelationReq req){
        return doRequest(() -> restClient.addFriend(req).execute());
    }

    public String updateFriendRelation(RelationReq req){
        return doRequest(() -> restClient.updateFriendRelation(req).execute());
    }

    public GroupInfoVO createGroup(GroupReq req){
        return doRequest(() -> restClient.createGroup(req).execute());
    }

    public GroupMemberVO inviteUser(GroupReq req){
        return doRequest(() -> restClient.inviteUser(req).execute());
    }

    public List<GroupInfoVO> groups(UserReq req) {
        return doRequest(() -> restClient.groups(req).execute());
    }

    public List<GroupMemberVO> groupMembers(GroupReq req){
        return doRequest(() -> restClient.groupMembers(req).execute());
    }

    public List<GroupOfflineMsgVO> pollGroupOffline(GroupReq req){
        return doRequest(() -> restClient.pollGroupOffline(req).execute());
    }

}
