package com.mmall.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.Resource;

/**
 * @author liliang
 * @date 2017/11/29.
 */
@Slf4j
@Service("redisPool")
public class RedisPool {

    @Resource(name = "shardedJedisPool")
    private ShardedJedisPool shardedJedisPool;

    /**
     * 获取实例
     *
     * @return
     */
    public ShardedJedis instance() {
        return shardedJedisPool.getResource();
    }

    /**
     * 关闭 redis 连接池
     * @param shardedJedis
     */
    public void safeClose(ShardedJedis shardedJedis) {
        try {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        } catch (Exception e) {
            log.error("return redis resource exception", e);
        }
    }
}
