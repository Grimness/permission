package com.mmall.service;

import com.mmall.beans.CacheKeyConstants;

/**
 * @author liliang
 * @date 2017/11/29.
 */
public interface SysCacheService {

    /**
     * 保存内容至缓存
     *
     * @param toSavedValue   要保存的值
     * @param timeoutSeconds 超时时间
     * @param prefix         key 前缀
     * @param keys           key
     */
    void saveCache(String toSavedValue, int timeoutSeconds, CacheKeyConstants prefix, String... keys);

    /**
     * 获取缓存内容
     *
     * @param prefix
     * @param keys
     * @return
     */
    String getFromCache(CacheKeyConstants prefix, String... keys);

}
