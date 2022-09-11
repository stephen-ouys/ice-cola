package cn.stephen12.icecola.component.utils.fastjson.enhance;

import com.alibaba.fastjson.serializer.JSONSerializer;

import java.lang.reflect.Type;

/**
 * JSON序列化拦截器
 *
 * @author ouyangsheng
 * @date 2022-04-29
 **/
public interface SerializeInterceptor {

    /**
     * 序列化前置处理器
     *
     * @param jsonSerializer
     * @param bean
     * @param fieldName 引用这个Bean 的列名
     * @param fieldType 引用这个Bean 的列类型
     * @param features
     * @return 是否继续序列化
     */
    boolean beforeSerializing(JSONSerializer jsonSerializer, Object bean, Object fieldName, Type fieldType, int features);

}
