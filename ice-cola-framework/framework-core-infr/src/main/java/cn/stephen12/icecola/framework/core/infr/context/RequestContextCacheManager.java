package cn.stephen12.icecola.framework.core.infr.context;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 请求上下文（基于线程变量）缓存管理器
 *
 * @author ouyangsheng
 * @date 2022-06-24
 **/
@Component
public class RequestContextCacheManager implements CacheManager {
    private final static String CACHE_MAP_KEY = "REQUEST_CONTEXT_CACHE_MAP";

    @Override
    public Cache getCache(String name) {
        //获取全部Cache对象
        Map<String, ConcurrentMapCache> cacheMap = getCacheMap();

        //如果已存在则获取，不存在则新建
        return cacheMap.computeIfAbsent(name, key -> new ConcurrentMapCache(key, true));
    }

    @Override
    public Collection<String> getCacheNames() {
        return getCacheMap().keySet();
    }

    /**
     * Map 装载 当前请求 所有有效的缓存
     * @return
     */
    private Map<String, ConcurrentMapCache> getCacheMap() {
        return RequestContext.computeIfAbsent(CACHE_MAP_KEY, key -> new ConcurrentHashMap<>(16));
    }
}
