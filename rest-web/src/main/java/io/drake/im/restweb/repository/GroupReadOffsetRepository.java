package io.drake.im.restweb.repository;

import io.drake.im.restweb.domain.entity.GroupReadOffset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Date: 2021/05/10/16:31
 *
 * @author : Drake
 * Description:
 */
@Repository
public interface GroupReadOffsetRepository extends JpaRepository<GroupReadOffset, Long> {

    GroupReadOffset findByGroupIdAndUserId(Long groupId, String userId);

}
