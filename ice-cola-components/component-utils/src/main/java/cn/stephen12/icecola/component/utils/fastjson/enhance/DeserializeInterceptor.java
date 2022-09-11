package cn.stephen12.icecola.component.utils.fastjson.enhance;

import com.alibaba.fastjson.parser.DefaultJSONParser;

import java.lang.reflect.Type;

/**
 * JSON序列化拦截器
 * @author ouyangsheng
 * @date 2022-04-29
 **/
public interface DeserializeInterceptor {

    /**
     * 反序列化前置处理器
     * @param parser
     * @param type
     * @param fieldName
     * @return
     */
    boolean beforeDeserializing(DefaultJSONParser parser,
                             Type type,
                             Object fieldName);

    /**
     * 反序列化后置处理
     * @param result 反序列化结果
     * @param parser 转换器
     * @param type 类型
     * @param fieldName 属性名
     * @return
     */
    Object afterDeserialized(Object result,
                           DefaultJSONParser parser,
                           Type type,
                           Object fieldName);
}
