package io.drake.im.restweb.repository;

import io.drake.im.restweb.domain.entity.GroupInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Date: 2021/05/10/16:29
 *
 * @author : Drake
 * Description:
 */
@Repository
public interface GroupInfoRepository extends JpaRepository<GroupInfo, Long> {

}
