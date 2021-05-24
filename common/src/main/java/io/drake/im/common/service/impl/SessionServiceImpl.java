package io.drake.im.common.service.impl;

import io.drake.im.common.service.SessionService;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Date: 2021/04/21/10:18
 *
 * @author : Drake
 * Description:
 */
@Slf4j
public class SessionServiceImpl implements SessionService {

    private JedisPool jedisPool;

    public SessionServiceImpl(String host, Integer port, String password){
        JedisPoolConfig config = new JedisPoolConfig();
        jedisPool = new JedisPool(config, host, port, 2*1000,password);
    }


    @Override
    public String online(String userId, String connId) {
        log.debug("userId:[{}] online with connectId [{}]", userId, connId);
        Jedis jedis = jedisPool.getResource();
        //会话由keeplive控制，但其失效时redis中可能仍有数据
        jedis.hdel(SESSION_KEY, userId);
        jedis.hset(SESSION_KEY, userId, connId);


        return connId;
    }

    @Override
    public String offline(String userId) {
        Jedis jedis = jedisPool.getResource();

        jedis.hdel(SESSION_KEY, userId);

        return null;
    }

    @Override
    public Boolean isOnline(String userId) {
        Jedis jedis = jedisPool.getResource();

        String session = jedis.hget(SESSION_KEY, userId);

        return null != session;
    }
}
