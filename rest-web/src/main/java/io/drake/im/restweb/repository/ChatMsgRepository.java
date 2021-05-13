package io.drake.im.restweb.repository;

import io.drake.im.restweb.domain.entity.ChatMsg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Date: 2021/04/22/16:02
 *
 * @author : Drake
 * Description:
 */
@Repository
public interface ChatMsgRepository extends JpaRepository<ChatMsg, Long> {

    List<ChatMsg> findByDestAndIsSigned(String dest, Boolean isSigned);

}
