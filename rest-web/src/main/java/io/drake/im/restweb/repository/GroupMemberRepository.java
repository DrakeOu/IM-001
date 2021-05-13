package io.drake.im.restweb.repository;

import io.drake.im.restweb.domain.entity.GroupMember;
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
public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {

    GroupMember findByGroupIdAndUserId(Long groupId, String userId);

    List<GroupMember> findByUserId(String userId);

    List<GroupMember> findByGroupId(Long groupId);

}
