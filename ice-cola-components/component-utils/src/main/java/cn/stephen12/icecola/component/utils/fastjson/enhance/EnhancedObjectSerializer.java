package cn.stephen12.icecola.component.utils.fastjson.enhance;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.JavaBeanSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeBeanInfo;
import com.alibaba.fastjson.util.TypeUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 增强版ObjectSerializer
 *
 * @author 欧阳胜
 * @date 2022-04-29
 */
public class EnhancedObjectSerializer implements ObjectSerializer {

    private static final Map<Class<?>, ObjectSerializer> javaBeanSerializerCache = new ConcurrentHashMap<>(16);

    @Override
    public void write(JSONSerializer serializer, Object bean, Object fieldName, Type fieldType, int features) throws IOException {
        serializer.getWriter().append("{");
        //前置处理器
        JsonInterceptors.beforeSerializing(serializer, bean, fieldName, fieldType, features);

        getOrCreateJavaBeanSerializer(serializer, bean.getClass())
                .write(serializer, bean, fieldName, fieldType, features);

        //后置处理器
        JsonInterceptors.afterSerialized(serializer, bean, fieldName, fieldType, features);

        serializer.getWriter().append("}");
    }


    private ObjectSerializer getOrCreateJavaBeanSerializer(JSONSerializer serializer, Class<?> clazz) {
        if (!javaBeanSerializerCache.containsKey(clazz)) {
            synchronized (EnhancedObjectSerializer.class) {
                SerializeBeanInfo beanInfo = TypeUtils.buildBeanInfo(clazz, null, serializer.getMapping().propertyNamingStrategy, BeanUtil.getProperty(serializer.getMapping(),"fieldBased"));
                ObjectSerializer javaBeanSerializer = new MyJavaBeanSerializer(beanInfo);
                javaBeanSerializerCache.put(clazz, javaBeanSerializer);
            }
        }
        return javaBeanSerializerCache.get(clazz);
    }

    /**
     * 重新该类的目的在于：
     * 避免父类生成左右括号
     */
    class MyJavaBeanSerializer extends JavaBeanSerializer {
        public MyJavaBeanSerializer(SerializeBeanInfo beanInfo) {
            super(beanInfo);
        }

        @Override
        public void write(JSONSerializer serializer, //
                          Object object, //
                          Object fieldName, //
                          Type fieldType, //
                          int features) throws IOException {
            write(serializer, object, fieldName, fieldType, features, true);
        }

    }
}
