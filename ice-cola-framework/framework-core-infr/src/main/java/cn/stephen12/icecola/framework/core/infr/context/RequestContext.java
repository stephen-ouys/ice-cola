package cn.stephen12.icecola.framework.core.infr.context;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 基础设施层，请求上下文
 *
 * @author ouyangsheng
 * @date 2022-05-23
 **/
public class RequestContext {

    private static final ThreadLocal<Map<String, Object>> requestCache = new ThreadLocal<>();

    static void init() {
        requestCache.set(new HashMap<>(16));
    }

    static void clear() {
        requestCache.remove();
    }

    /**
     * 所有对缓存的操作，需要调用此接口
     * @return
     */
    static Map<String, Object> getOrInit() {
        if (requestCache.get() == null) {
            init();
        }
        return requestCache.get();
    }

    public static void put(String key, Object value) {
        getOrInit().put(key, value);
    }

    public static <T> T getObj(String key) {
        return (T) getOrInit().get(key);
    }

    public static <T> T computeIfAbsent(String key, Function<String, T> value) {
        return (T) getOrInit().computeIfAbsent(key, value);
    }

    public static String getString(String key) {
        return (String) getOrInit().get(key);
    }

    public static Long getLong(String key) {
        return (Long) getOrInit().get(key);
    }
}
