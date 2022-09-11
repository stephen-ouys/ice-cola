package cn.stephen12.icecola.component.utils.fastjson.enhance;

import com.alibaba.cola.domain.ApplicationContextHelper;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.serializer.JSONSerializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Json 拦截器注册中心
 *
 * @author ouyangsheng
 * @date 2022-04-29
 **/
public class JsonInterceptors {
    private static List<DeserializeInterceptor> deserializeInterceptors;
    private static List<SerializeInterceptor> serializeInterceptors;

    private static void init() {
        deserializeInterceptors = new ArrayList<>(ApplicationContextHelper.getApplicationContext().getBeansOfType(DeserializeInterceptor.class).values());
        serializeInterceptors = new ArrayList<>(ApplicationContextHelper.getApplicationContext().getBeansOfType(SerializeInterceptor.class).values());
    }

    /**
     * 反序列化前置处理器
     *
     * @param parser
     * @param type
     * @param fieldName
     * @return
     */
    public static boolean beforeDeserializing(DefaultJSONParser parser,
                                              Type type,
                                              Object fieldName) {

        for (DeserializeInterceptor item : getDeserializeInterceptors()) {
            //只要有人喊停，就停
            if (!item.beforeDeserializing(parser, type, fieldName)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 反序列化后置处理
     *
     * @param result    反序列化结果
     * @param parser    转换器
     * @param type      类型
     * @param fieldName 属性名
     */
    public static void afterDeserialized(Object result,
                                         DefaultJSONParser parser,
                                         Type type,
                                         Object fieldName) {


        getDeserializeInterceptors().forEach(item -> item.afterDeserialized(result, parser, type, fieldName));
    }

    private static List<DeserializeInterceptor> getDeserializeInterceptors() {
        if (deserializeInterceptors == null) {
            synchronized (JsonInterceptors.class) {
                init();
            }
        }
        return deserializeInterceptors;
    }

    /**
     * 序列化前置处理器
     *
     * @param jsonSerializer
     * @return
     */
    public static void beforeSerializing(JSONSerializer jsonSerializer, Object bean, Object fieldName, Type fieldType, int features) {
        getSerializeInterceptors().forEach(item -> item.beforeSerializing(jsonSerializer, bean, fieldName, fieldType, features));
    }

    /**
     * 序列化后置处理器
     *
     * @param jsonSerializer
     * @return
     */
    public static void afterSerialized(JSONSerializer jsonSerializer, Object bean, Object fieldName, Type fieldType, int features) {
    }

    private static List<SerializeInterceptor> getSerializeInterceptors() {
        if (serializeInterceptors == null) {
            synchronized (JsonInterceptors.class) {
                init();
            }
        }
        return serializeInterceptors;
    }

}
