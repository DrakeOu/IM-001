package io.drake.im.restweb.repository;

import io.drake.im.restweb.domain.entity.UserRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Date: 2021/05/07/20:52
 *
 * @author : Drake
 * Description:
 */
@Repository
public interface UserRelationRepository extends JpaRepository<UserRelation, String> {

    UserRelation findByUserAAndUserB(String userA, String userB);

    List<UserRelation> findByUserAAndStatus(String userA, String status);

}
