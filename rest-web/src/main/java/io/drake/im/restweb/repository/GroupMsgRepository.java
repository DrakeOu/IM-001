package io.drake.im.restweb.repository;

import io.drake.im.restweb.domain.entity.GroupMsg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Date: 2021/05/10/16:30
 *
 * @author : Drake
 * Description:
 */
@Repository
public interface GroupMsgRepository extends JpaRepository<GroupMsg, Long> {

    List<GroupMsg> findByGroupIdAndMsgOffsetAfter(Long groupId, Long offset);

    GroupMsg findFirstByGroupIdOrderByMsgOffsetDesc(Long groupId);


}
