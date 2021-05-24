package io.drake.im.restweb.service.impl;

import io.drake.im.common.constant.RelationCmdEnum;
import io.drake.im.common.domain.http.vo.RelationVO;
import io.drake.im.common.exception.IMException;
import io.drake.im.common.utils.IdUtil;
import io.drake.im.restweb.constant.UserRelationEnum;
import io.drake.im.restweb.domain.entity.User;
import io.drake.im.restweb.domain.entity.UserRelation;
import io.drake.im.restweb.repository.UserRelationRepository;
import io.drake.im.restweb.repository.UserRepository;
import io.drake.im.restweb.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Date: 2021/04/19/17:01
 *
 * @author : Drake
 * Description:
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserRelationRepository userRelationRepository;

    public UserServiceImpl(UserRepository userRepository, UserRelationRepository userRelationRepository) {
        this.userRepository = userRepository;
        this.userRelationRepository = userRelationRepository;
    }

    @Override
    public User register(String userName, String password) {
        User user = userRepository.findByUserName(userName);
        if(null != user){

        }else{
            User user1 = new User();
            user1.setUserName(userName);
            user1.setPassword(password);
            //todo ID生成器
            String userId = IdUtil.UUID();
            user1.setUserId(userId);
            userRepository.save(user1);
            user = user1;
        }
        return user;
    }

    @Override
    public User login(String userName, String password) {
        User user = userRepository.findByUserName(userName);
        if(null == user){
            //todo
        }else if(!password.equals(user.getPassword())){
            user = null;
            log.debug("user: {} login fail of wrong password", userName);
        }

        return user;
    }

    //return list of friends or empty list
    @Override
    public List<RelationVO> friends(String userId) {
        List<RelationVO> friends = new ArrayList<>();
        List<UserRelation> actualRelation = userRelationRepository.findByUserAAndStatus(userId, UserRelationEnum.DONE.name());
        actualRelation.forEach(a -> friends.add(new RelationVO(a.getUserA(), a.getUserB(), a.getUserNameA(), a.getUserNameB(), null)));
        return friends;
    }

    @Override
    public UserRelation addFriend(String userIdA, String userIdB) {
        User userA = userRepository.findByUserId(userIdA);
        User userB = userRepository.findByUserId(userIdB);
        if(null == userA || null == userB){
            throw new IMException("userA or B is not exist");
        }
        UserRelation relation = userRelationRepository.findByUserAAndUserB(userIdA, userIdB);
        if(null == relation){
            relation = new UserRelation(userA.getUserId(), userB.getUserId(), userA.getUserName(), userB.getUserName(), UserRelationEnum.TODO.name(), new Date());
            userRelationRepository.save(relation);
        }else if(UserRelationEnum.DENY.name().equals(relation.getStatus())){
            relation.setStatus(UserRelationEnum.TODO.name());
            userRelationRepository.save(relation);
        }
        return relation;
    }

    //todo 考虑通知在线用户
    @Override
    @Transactional
    public void updateFriendRelation(String userNameA, String userNameB, RelationCmdEnum cmd) {
        UserRelation relation = userRelationRepository.findByUserNameAAndUserNameB(userNameA, userNameB);
        if(null == relation){
            throw new IMException("RELATION OPERATED IS NOT EXIST");
        }else{
            if(cmd == RelationCmdEnum.ACCEPT){
                relation.setStatus(UserRelationEnum.DONE.name());
            }else if(cmd == RelationCmdEnum.DENY){
                relation.setStatus(UserRelationEnum.DENY.name());
            }else if(cmd == RelationCmdEnum.BLOCK){
                relation.setStatus(UserRelationEnum.BLOCK.name());
            }else{

            }
            userRelationRepository.save(relation);
        }
    }



    @Override
    public User findByName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    public Boolean existUser(String userName) {
        return userRepository.findByUserName(userName) != null;
    }

    @Override
    public Boolean isFriend(String userIdA, String userIdB) {
        UserRelation relationA = userRelationRepository.findByUserAAndUserB(userIdA, userIdB);
        UserRelation relationB = userRelationRepository.findByUserAAndUserB(userIdB, userIdA);
        return (null != relationA && UserRelationEnum.DONE.name().equals(relationA.getStatus())) ||
                (null != relationB && UserRelationEnum.DONE.name().equals(relationB.getStatus()));
    }


}
