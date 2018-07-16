package org.randomchat.common.cache.caffeineredis;

import java.io.Serializable;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.randomchat.common.cache.CacheChangeType;
import org.randomchat.common.cache.CacheChangedVo;
import org.randomchat.common.cache.ICache;
import org.randomchat.common.cache.IL2Cache;
import org.randomchat.common.cache.caffeine.CaffeineCache;
import org.randomchat.common.cache.redis.JedisTemplate;
import org.randomchat.common.cache.redis.RedisCache;
import org.randomchat.common.cache.redis.RedisExpireUpdateTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Leo Yang
 * 2017年8月12日 下午9:13:54
 */
public class CaffeineRedisCache implements ICache,IL2Cache {
	
	Logger log = LoggerFactory.getLogger(CaffeineRedisCache.class);
	
	CaffeineCache caffeineCache;

	RedisCache redisCache;

	String cacheName;

	/**
	 *
	 * @author Leo Yang
	 */
	public CaffeineRedisCache() {
	}

	/**
	 * @param caffeineCache
	 * @param redisCache
	 * @author Leo Yang
	 */
	public CaffeineRedisCache(String cacheName, CaffeineCache caffeineCache, RedisCache redisCache) {
		super();
		this.cacheName = cacheName;
		this.caffeineCache = caffeineCache;
		this.redisCache = redisCache;
	}

	/**
	 *
	 * @author Leo Yang
	 */
	@Override
	public void clear() {
		try {
			caffeineCache.clear();
			redisCache.clear();
			CacheChangedVo cacheChangedVo = new CacheChangedVo(cacheName, CacheChangeType.CLEAR);
			JedisTemplate.me().publish(CaffeineRedisCacheManager.CACHE_CHANGE_TOPIC,cacheChangedVo.toString());
		} catch (Exception e) {
			log.error(e.toString(),e);
		}
	}

	/**
	 * @param key
	 * @return
	 * @author Leo Yang
	 */
	@Override
	public Serializable get(String key) {
		if (StringUtils.isBlank(key)) {
			return null;
		}
		
		Serializable ret = caffeineCache.get(key);
		if (ret == null) {
			ret = redisCache.get(key);
			if (ret != null) {
				log.debug("Cache L2 (redis) :{}={}",key,ret);
				caffeineCache.put(key, ret);
			}
		} else {//在本地就取到数据了，那么需要在redis那定时更新一下过期时间
			log.debug("Cache L1 (caffeine) :{}={}",key,ret);
			Integer timeToIdleSeconds = redisCache.getTimeToIdleSeconds();
			if (timeToIdleSeconds != null) {
				RedisExpireUpdateTask.add(cacheName, key, ret , timeToIdleSeconds);
			}
		}
		return ret;
	}

	/**
	 * @return
	 * @author Leo Yang
	 */
	@Override
	public Collection<String> keys() {
		return redisCache.keys();
	}

	/**
	 * @param key
	 * @param value
	 * @author Leo Yang
	 */
	@Override
	public void put(String key, Serializable value) {
		try {
			caffeineCache.put(key, value);
			redisCache.put(key, value);
			CacheChangedVo cacheChangedVo = new CacheChangedVo(cacheName, key, CacheChangeType.PUT);
			JedisTemplate.me().publish(CaffeineRedisCacheManager.CACHE_CHANGE_TOPIC,cacheChangedVo.toString());
		} catch (Exception e) {
			log.error(e.toString(),e);
		}
	}
	
	@Override
	public void putL2Async(String key, Serializable value) {
		caffeineCache.put(key, value);
		CaffeineRedisCacheManager.getAsyncRedisQueue().add(new RedisL2Vo(redisCache, key, value));
	}
	
	@Override
	public void putTemporary(String key, Serializable value) {
		caffeineCache.putTemporary(key, value);
		redisCache.putTemporary(key, value);
	}

	/**
	 * @param key
	 * @author Leo Yang
	 */
	@Override
	public void remove(String key) {
		if (StringUtils.isBlank(key)) {
			return;
		}
		try{
			caffeineCache.remove(key);
			redisCache.remove(key);
			CacheChangedVo cacheChangedVo = new CacheChangedVo(cacheName, key, CacheChangeType.REMOVE);
			JedisTemplate.me().publish(CaffeineRedisCacheManager.CACHE_CHANGE_TOPIC,cacheChangedVo.toString());
		} catch (Exception e) {
			log.error(e.toString(),e);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(String key, Class<T> clazz) {
		return (T)get(key);
	}

	public CaffeineCache getCaffeineCache() {
		return caffeineCache;
	}

	public RedisCache getRedisCache() {
		return redisCache;
	}

}
