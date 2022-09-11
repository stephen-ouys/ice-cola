package cn.stephen12.icecola.component.utils.fastjson.enhance;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * BigDecimal格式化
 *
 * @author jiangxinjun
 * @date 2020/06/03
 */
public class EnhancedObjectDeserializer implements ObjectDeserializer {
    private static final Map<Class<?>, JavaBeanDeserializer> javaBeanDeserializerCache = new ConcurrentHashMap<>(16);

    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        JsonInterceptors.beforeDeserializing(parser, type, fieldName);

        T result = getOrCreateJavaBeanDeserializer((Class<?>) type).deserialze(parser, type, fieldName);
        JsonInterceptors.afterDeserialized(result,parser,type,fieldName);

        return result;
    }

    @Override
    public int getFastMatchToken() {
        return 0;
    }

    private JavaBeanDeserializer getOrCreateJavaBeanDeserializer(Class<?> clazz) {
        if (!javaBeanDeserializerCache.containsKey(clazz)) {
            synchronized (EnhancedObjectSerializer.class) {
                JavaBeanDeserializer javaBeanSerializer = new JavaBeanDeserializer(ParserConfig.getGlobalInstance(),clazz);
                javaBeanDeserializerCache.put(clazz, javaBeanSerializer);
            }
        }
        return javaBeanDeserializerCache.get(clazz);
    }
}
