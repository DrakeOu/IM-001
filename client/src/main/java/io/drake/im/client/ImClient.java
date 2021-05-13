package io.drake.im.client;

import io.drake.im.client.api.RestClientService;
import io.drake.im.common.domain.http.req.GroupReq;
import io.drake.im.common.domain.http.req.RelationReq;
import io.drake.im.client.handler.ClientToConnectorHandler;
import io.drake.im.client.websocket.ClientWSListener;
import io.drake.im.client.websocket.WebSocketClient;
import io.drake.im.common.codec.*;
import io.drake.im.common.config.WebSocketConfiguration;
import io.drake.im.common.constant.RelationCmdEnum;
import io.drake.im.common.domain.http.req.UserReq;
import io.drake.im.common.domain.http.vo.*;
import io.drake.im.common.utils.MD5Util;
import io.drake.im.protobuf.generate.IMInternal;
import io.drake.im.protobuf.generate.IMMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class ImClient {

    private RestClientService restClientService;

    private ChannelFuture channelFuture;

    private UserInfoVO curUser;

    private Map<String, RelationVO> friendMap = new HashMap<>();

    private Map<String, GroupInfoVO> groupMap = new HashMap<>();

    private WebSocketClient wsClient;

    public ImClient(RestClientService restClientService){
        this.restClientService = restClientService;
    }

    public void login(String userName, String password){
        doLogin(userName, password);
        //todo retry
        connectToNetty();
        greetToConnector();
        getGroups();
        connectToWs();
        pollOffline();
        pollAllGroupOffline();

    }

    private void doLogin(String userName, String password){
        UserInfoVO userInfo = restClientService.login(userName, MD5Util.encrypt(password));
        if(null == userInfo){
            log.error("USERNAME OR PASSWORD ERROR");
            return;
        }
        curUser = userInfo;
        log.debug("WEB SERVER REPLY SUCCESS, CONNECTING TO NETTY SERVER");
    }

    public void register(String userName, String password){
        if(!StringUtils.hasText(userName) || !StringUtils.hasText(password)){
            log.debug("USERNAME OR PASSWORD CANNOT BE EMPTY");
            return;
        }
        UserInfoVO user = restClientService.register(userName, MD5Util.encrypt(password));

        login(user.getUserName(), MD5Util.encrypt(password));
    }

    private void connectToNetty() {
        EventLoopGroup loopGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        ChannelFuture future = bootstrap.group(loopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new MsgEncoder());
                        pipeline.addLast(new MsgDecoder());
                        pipeline.addLast(new ClientToConnectorHandler());

                    }
                }).connect(curUser.getNettyAddress()).addListener((ChannelFutureListener) future1 -> {
                    if(future1.isSuccess()){
                        log.debug("SUCCESSFULLY CONNECT TO NETTY SERVER");
                    }else{
                        log.error("FAIL CONNECT TO NETTY SERVER");
                    }
                });

        try {
            future.get(10, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }
        this.channelFuture = future;
    }




    public void sendMsgToFriend(String userName, String msg){
        //before send
        getFriends();

        if(StringUtils.isEmpty(userName) || !friendMap.containsKey(userName)){
            log.debug("please enter friend name or user is not your friend");
            return;
        }
        IMMessage.ChatMsg chatMsg = IMMessage.ChatMsg.newBuilder()
                .setOriginId(curUser.getUserId())
                .setDestId(friendMap.get(userName).getUserB())
                .setOriginName(curUser.getUserName())
                .setContent(msg)
                .setCreateTime(new Date().getTime())
                .setType(IMMessage.ChatMsg.MessageType.SINGLE)
                .build();

        channelFuture.channel().writeAndFlush(chatMsg);

    }

    public void sendMsgToGroup(String groupName, String msg){
        //before send
        getGroups();

        if(!groupMap.containsKey(groupName)){
            log.debug("you are not in group {}", groupName);
            return;
        }
        GroupInfoVO groupInfoVO = groupMap.get(groupName);
        List<String> members = getGroupMembers(groupInfoVO.getId()).stream().map(GroupMemberVO::getUserId).collect(Collectors.toList());

        IMMessage.ChatMsg groupMsg = IMMessage.ChatMsg.newBuilder()
                .setOriginId(curUser.getUserId())
                .setDestId(groupInfoVO.getId().toString())
                .setOriginName(curUser.getUserName())
                .addAllGroupMembers(members)
                .setContent(msg)
                .setCreateTime(new Date().getTime())
                .setType(IMMessage.ChatMsg.MessageType.GROUP)
                .build();

        channelFuture.channel().writeAndFlush(groupMsg);
    }

    private void getGroups() {
        UserReq req = new UserReq();
        req.setUserId(curUser.getUserId());
        req.setUserName(curUser.getUserName());
        List<GroupInfoVO> groups = restClientService.groups(req);
        groups.forEach(g -> groupMap.put(g.getName(), g));
    }

    public List<GroupMemberVO> getGroupMembers(Long groupId){
        return restClientService.groupMembers(GroupReq.ofGroupMember(curUser, groupId));
    }

    public List<RelationVO> getFriends(){
        UserInfoVO userInfo = restClientService.friends(curUser.getUserId());
        List<RelationVO> friends = null == userInfo ? new ArrayList<>() : userInfo.getFriends();
        //refresh friend list
        if(null == friendMap){
            friendMap = new HashMap<>();
        }
        friends.forEach(x -> friendMap.put(x.getUserNameB(), x));
        return friends;
    }

    public boolean isLogin(){
        return null != curUser;
    }

    public void offline() {
        channelFuture.channel().close();
        friendMap = null;
        wsClient.close();
        log.debug("YOU ARE OFFLINE NOW");
    }

    public void addFriend(String userName){
        if(friendMap.containsKey(userName)){
            log.debug("{} is already your friend", userName);
        }else{
            RelationReq req = RelationReq.ofApplyFriend(curUser, userName);
            String result = restClientService.addFriend(req);
            log.debug("{}", result);
        }
    }

    public void copeFriendRequest(String userName, String command){
        if(friendMap.containsKey(userName)){
            log.debug("{} is already your friend", userName);
            return;
        }else if(RelationCmdEnum.checkValid(command)){
            String result = restClientService.updateFriendRelation(RelationReq.ofUpdateFriend(curUser, userName, command));
            log.debug("RESULT: {}", result);
        }else{
            log.debug("{} is not valid", command);
        }

    }

    public void createGroup(String groupName){
        GroupInfoVO group = restClientService.createGroup(GroupReq.ofNewGroup(curUser, groupName));
        if(null == group){
            log.error("UNKNOWN ERROR GROUP CREATE FAIL");
        }else{
            log.debug("GROUP [{}] IS CREATED WITH GROUPID [{}]", group.getName(), group.getId());
        }
    }

    public void inviteUser(String userName, Long groupId){
        GroupMemberVO memberVO = restClientService.inviteUser(GroupReq.ofInviteUser(curUser, userName, groupId));
        if(null == memberVO){
            log.error("INVITE FAIL");
        }else{
            log.debug("USER [{}] IS NOW MEMBER OF GROUP [{}]", memberVO.getUserName(), memberVO.getGroupId());
        }
    }

    private void pollOffline(){
        log.debug("POLLING OFFLINE MSG...");
        List<OfflineMsgVO> offlineMsgs = restClientService.pollOffline(curUser.getUserId());
        for(OfflineMsgVO offlineMsg: offlineMsgs){
            log.debug("[{}]-[{}]: {}", offlineMsg.getOrigin(), offlineMsg.getCreateAt(), offlineMsg.getContent());
        }
    }

    public void pollAllGroupOffline(){
        log.debug("POLLING GROUP OFFLINE MSG...");
        for(Map.Entry<String, GroupInfoVO> group: groupMap.entrySet()){
            GroupInfoVO infoVO = group.getValue();
           pollGroupOffline(infoVO.getId());
        }
    }

    public void pollGroupOffline(Long groupId){
        GroupReq req = new GroupReq();
        req.setGroupId(groupId);
        req.setUserId(curUser.getUserId());
        List<GroupOfflineMsgVO> msgVOS = restClientService.pollGroupOffline(req);
        for(GroupOfflineMsgVO offlineMsgVO: msgVOS){
            log.debug("[{}]-[{}]-[{}]: {}", offlineMsgVO.getGroupName(), offlineMsgVO.getSenderName(), offlineMsgVO.getCreateTime(), offlineMsgVO.getContent());
        }
    }

    private void greetToConnector(){
        //首次Ack时将在nettyServer端创建conn
        log.debug("GREET TO NETTY SERVER");
        IMInternal.Internal greet = IMInternal.Internal.newBuilder()
                .setMsgType(IMInternal.Internal.MsgType.GREET)
                .setOrigin(IMInternal.Internal.Module.CLIENT)
                .setDest(IMInternal.Internal.Module.CONNECTOR)
                .setBody(curUser.getUserId())
                .build();
        channelFuture.channel().writeAndFlush(greet);
    }

    private void connectToWs(){
        log.debug("CONNECTING TO WEBSOCKET");
        wsClient = new WebSocketClient(curUser.getWsUrl()+"/"+curUser.getUserId(), new WebSocketConfiguration(), new ClientWSListener());
    }
}
