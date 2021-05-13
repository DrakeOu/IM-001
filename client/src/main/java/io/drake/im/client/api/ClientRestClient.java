package io.drake.im.client.api;

import io.drake.im.common.domain.http.req.GroupReq;
import io.drake.im.common.domain.http.req.RelationReq;
import io.drake.im.common.domain.http.req.UserReq;
import io.drake.im.common.domain.http.vo.*;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

import java.util.List;

/**
 * Date: 2021/04/20/19:40
 *
 * @author : Drake
 * Description:
 */
public interface ClientRestClient {

    @Headers("Content-Type: application/json")
    @POST("login")
    Call<RestResult<UserInfoVO>> login(@Body UserReq body);

    @POST("register")
    Call<RestResult<UserInfoVO>> register(@Body UserReq body);

    @POST("friends")
    Call<RestResult<UserInfoVO>> friends(@Body UserReq body);

    @POST("pollOfflineMsg")
    Call<RestResult<List<OfflineMsgVO>>> pollOfflineMsg(@Body UserReq body);

    @POST("addFriend")
    Call<RestResult<String>> addFriend(@Body RelationReq body);

    @POST("updateFriendRelation")
    Call<RestResult<String>> updateFriendRelation(@Body RelationReq body);

    @POST("createGroup")
    Call<RestResult<GroupInfoVO>> createGroup(@Body GroupReq body);

    @POST("inviteUser")
    Call<RestResult<GroupMemberVO>> inviteUser(@Body GroupReq body);

    @POST("groups")
    Call<RestResult<List<GroupInfoVO>>> groups(@Body UserReq body);

    @POST("groupMembers")
    Call<RestResult<List<GroupMemberVO>>> groupMembers(@Body GroupReq body);

    @POST("pollGroupOffline")
    Call<RestResult<List<GroupOfflineMsgVO>>> pollGroupOffline(@Body GroupReq body);
}
