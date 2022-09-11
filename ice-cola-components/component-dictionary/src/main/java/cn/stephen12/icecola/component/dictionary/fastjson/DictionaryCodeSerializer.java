package cn.stephen12.icecola.component.dictionary.fastjson;

import cn.stephen12.icecola.component.dictionary.model.Dictionary;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;

import java.lang.reflect.Type;

/**
 * 字典编码序列化器
 *
 * @author ouyangsheng
 * @date 2022-03-17
 */
public class DictionaryCodeSerializer implements ObjectSerializer {

    static final DictionaryCodeSerializer INSTANCE = new DictionaryCodeSerializer();

    @Override
    public void write(JSONSerializer jsonSerializer, Object object, Object fieldName, Type fieldType, int features) {
        SerializeWriter out = jsonSerializer.getWriter();
        if (object == null) {
            out.writeNull();
            return;
        }
        Dictionary dictionary = (Dictionary) object;

        if (dictionary.getCode() == null) {
            out.writeNull();
        } else if (dictionary.getCode() instanceof Number) {
            out.writeLong(((Number) dictionary.getCode()).longValue());
        } else if (dictionary.getCode() instanceof CharSequence) {
            out.writeString(String.valueOf( dictionary.getCode()));
        } else {
            out.writeString(String.valueOf(dictionary.getCode()));
        }
    }
}
