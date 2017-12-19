package com.mmall.service.impl;

import com.google.common.base.Joiner;
import com.mmall.beans.CacheKeyConstants;
import com.mmall.service.SysCacheService;
import com.mmall.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;

import javax.annotation.Resource;

/**
 * @author liliang
 * @date 2017/11/29.
 */
@Slf4j
@Service
public class SysCacheServiceImpl implements SysCacheService {

    @Resource(name = "redisPool")
    private RedisPool redisPool;

    public void saveCache(String toSavedValue, int timeoutSeconds, CacheKeyConstants prefix) {
        saveCache(toSavedValue, timeoutSeconds, prefix, "");
    }

    @Override
    public String getFromCache(CacheKeyConstants prefix, String... keys) {
        ShardedJedis shardedJedis = null;
        String cacheKey = generateCacheKey(prefix, keys);
        try {
            shardedJedis = redisPool.instance();
            return shardedJedis.get(cacheKey);
        } catch (Exception e) {
            log.error("获取缓存出现异常,prefix:{},keys{}", prefix.name(), JsonMapper.objToString(keys));
            return null;
        } finally {
            // 关闭连接
            redisPool.safeClose(shardedJedis);
        }
    }

    /**
     * 包装保存缓存方法
     *
     * @param toSavedValue   保存的值
     * @param timeoutSeconds 超时时间(秒)
     * @param prefix         前缀
     * @param keys           key
     */
    @Override
    public void saveCache(String toSavedValue, int timeoutSeconds, CacheKeyConstants prefix, String... keys) {
        if (toSavedValue == null) {
            return;
        }
        ShardedJedis shardedJedis = null;
        try {
            String cacheKey = generateCacheKey(prefix, keys);
            shardedJedis = redisPool.instance();
            shardedJedis.setex(cacheKey, timeoutSeconds, toSavedValue);
        } catch (Exception e) {
            log.error("保存缓存异常..,prefix:{},keys:{}", prefix.name(), JsonMapper.objToString(keys));
        } finally {
            // 关闭连接
            redisPool.safeClose(shardedJedis);
        }
    }

    /**
     * 组装key
     *
     * @return
     */
    private String generateCacheKey(CacheKeyConstants prefix, String... keys) {
        String key = prefix.name();
        if (keys != null && keys.length > 0) {
            key += "_" + Joiner.on("_").join(keys);
        }
        return key;
    }
}
