package io.drake.im.common.domain.conn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Date: 2021/04/19/14:39
 *
 * @author : Drake
 * Description:
 */
public class MemoryConnContext<C extends Conn> implements ConnContext<C>{

    private static final Logger logger = LoggerFactory.getLogger(MemoryConnContext.class);

    private final ConcurrentHashMap<Integer, C> connMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, C> userIdConnMap = new ConcurrentHashMap<>();

    @Override
    public C getConn(Integer id) {
        return connMap.get(id);
    }

    public C getConn(String userId){
        return userIdConnMap.get(userId);
    }

    @Override
    public Boolean add(C conn) {
        logger.debug("add id: {} conn to ctx", conn.getConnId());
        connMap.put((Integer) conn.getConnId(), conn);
        return true;
    }

    public Boolean add(C conn, String userId){
        add(conn);
        userIdConnMap.put(userId, conn);
        return true;
    }

    @Override
    public Boolean remove(Integer id) {
        C remove = connMap.remove(id);
        return null != remove;
    }

    public Boolean remove(String userId){
        C remove = userIdConnMap.remove(userId);
        return null != remove;
    }

}
