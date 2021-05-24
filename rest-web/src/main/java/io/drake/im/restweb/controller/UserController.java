package io.drake.im.restweb.controller;

import io.drake.im.common.constant.RelationCmdEnum;
import io.drake.im.common.domain.http.req.RelationReq;
import io.drake.im.common.domain.http.req.UserReq;
import io.drake.im.common.domain.http.vo.RelationVO;
import io.drake.im.common.domain.http.vo.RestResult;
import io.drake.im.common.domain.http.vo.UserInfoVO;
import io.drake.im.restweb.config.ServerConfiguration;
import io.drake.im.restweb.constant.UserRelationEnum;
import io.drake.im.restweb.domain.entity.User;
import io.drake.im.restweb.domain.entity.UserRelation;
import io.drake.im.restweb.domain.ws.WSEvent;
import io.drake.im.restweb.service.UserService;
import io.drake.im.restweb.service.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * Date: 2021/04/19/16:57
 *
 * @author : Drake
 * Description:
 */
@RestController
@RequestMapping("/u")
public class UserController {

    private final UserService userService;

    private final WebSocketService webSocketService;

    private final ServerConfiguration serverConfiguration;

    public UserController(UserService userService, WebSocketService webSocketService, ServerConfiguration serverConfiguration) {
        this.userService = userService;
        this.webSocketService = webSocketService;
        this.serverConfiguration = serverConfiguration;
    }

    @RequestMapping("/register")
    public RestResult register(@RequestBody UserReq user){
        User register = userService.register(user.getUserName(), user.getPassword());
        return RestResult.success(register);
    }

    @RequestMapping("/login")
    public RestResult login(@RequestBody UserReq user){
        User login = userService.login(user.getUserName(), user.getPassword());
        if(null == login){
            return RestResult.fail("invalid username or password");
        }
        UserInfoVO userInfo = new UserInfoVO();
        userInfo.setUserId(login.getUserId());
        userInfo.setUserName(login.getUserName());
        userInfo.setNickName(login.getNickName());
        userInfo.setFriends(userService.friends(login.getUserId()));
        //返回的需要是容器的地址
        userInfo.setNettyAddress(InetSocketAddress.createUnresolved(serverConfiguration.getNettyServerHost(), serverConfiguration.getNettyServerPort()));
        userInfo.setWsUrl(serverConfiguration.getWsServerAddr());
        return RestResult.success(userInfo);
    }

    @RequestMapping("/logout")
    public RestResult logout(){
        return RestResult.success(null);
    }

    @RequestMapping("/friends")
    public RestResult friends(@RequestBody UserReq user){
        List<RelationVO> friends = userService.friends(user.getUserId());
        UserInfoVO userInfo = new UserInfoVO();
        userInfo.setFriends(friends);
        return RestResult.success(userInfo);
    }

    @RequestMapping("/addFriend")
    public RestResult addFriend(@RequestBody RelationReq req){
        if(req.getCmdType() != RelationCmdEnum.APPLY){
            return RestResult.fail("CMD TYPE IS NOT [APPLY] FOR ADD FRIEND");
        }
        String userIdA = req.getUserA();
        String userNameB = req.getUserNameB();
        User userB = userService.findByName(userNameB);

        UserRelation relation = userService.addFriend(userIdA, userB.getUserId());
        if(UserRelationEnum.DONE.name().equals(relation.getStatus())){
            return RestResult.success("已经是好友了");
        }else if(UserRelationEnum.DENY.name().equals(relation.getStatus())){
            return RestResult.fail("对方已拒绝");
        }else if(UserRelationEnum.BLOCK.name().equals(relation.getStatus())){
            return RestResult.fail("已被加入黑名单");
        }else if(UserRelationEnum.TODO.name().equals(relation.getStatus())){
            User temp = new User();
            temp.setUserId(userB.getUserId());
            temp.setUserName(req.getUserNameB());
            webSocketService.sendEvent(userB.getUserId(), WSEvent.ofFriend(temp, String.format("USER: %s request to be your friend", userNameB)));
            return RestResult.success("等待对方处理");
        }else{
            return RestResult.fail("UNKNOWN TYPE");
        }
    }

    @RequestMapping("/updateFriendRelation")
    public RestResult updateFriendRelation(@RequestBody RelationReq relation){
        //todo 有点问题
        if(!RelationCmdEnum.checkValid(relation.getCmdType().name())){
            return RestResult.fail("CMD TYPE IS ["+ relation.getCmdType().name() +"] NOT VALID");
        }
        User userB = userService.findByName(relation.getUserNameB());
        if(null == userB){
            return RestResult.fail(String.format("USER %s IS NOT EXIST", relation.getUserNameB()));
        }
        userService.updateFriendRelation(relation.getUserNameA(), userB.getUserName(), relation.getCmdType());
        return RestResult.success("已处理请求");
    }

}
