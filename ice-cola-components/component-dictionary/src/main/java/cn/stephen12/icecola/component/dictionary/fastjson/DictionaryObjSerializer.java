package cn.stephen12.icecola.component.dictionary.fastjson;

import cn.stephen12.icecola.component.dictionary.model.Dictionary;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;

import java.lang.reflect.Type;

/**
 * 字典对象输出序列化
 *
 * @author jiangxinjun
 * @date 2020/06/03
 */
public class DictionaryObjSerializer implements ObjectSerializer {

    static final DictionaryObjSerializer INSTANCE = new DictionaryObjSerializer();

    @Override
    public void write(JSONSerializer jsonSerializer, Object object, Object fieldName, Type fieldType, int features) {
        SerializeWriter out = jsonSerializer.getWriter();
        if (object == null) {
            out.writeNull();
            return;
        }
        Dictionary dictionary = (Dictionary) object;
        out.writeFieldValue('{',"code",String.valueOf(dictionary.getCode()));
        out.writeFieldValue(',',"name",String.valueOf(dictionary.getName()));
        out.write('}');
    }
}
