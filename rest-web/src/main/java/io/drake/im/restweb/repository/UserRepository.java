package io.drake.im.restweb.repository;

import io.drake.im.restweb.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Date: 2021/04/19/17:06
 *
 * @author : Drake
 * Description:
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {

    User findByUserName(String userName);

    User findByUserId(String userId);
}
