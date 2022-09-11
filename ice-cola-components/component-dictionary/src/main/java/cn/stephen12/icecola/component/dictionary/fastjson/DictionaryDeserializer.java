package cn.stephen12.icecola.component.dictionary.fastjson;

import cn.stephen12.icecola.component.dictionary.model.Dictionary;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Objects;

/**
 * BigDecimal格式化
 *
 * @author jiangxinjun
 * @date 2020/06/03
 */
public class DictionaryDeserializer implements ObjectDeserializer {
    static final DictionaryDeserializer INSTANCE = new DictionaryDeserializer();

    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        Object code = parser.parseObject(Object.class);
        Class<T> clazz = (Class) type;
        return getByCode(clazz,code);
    }

    @Override
    public int getFastMatchToken() {
        return 0;
    }

    private<T> T getByCode(Class<T> clazz ,Object code){
        return Arrays.stream(clazz.getEnumConstants()).filter(item->equalsOrStrEquals(code,((Dictionary)item).getCode())).findFirst().orElse(null);
    }
    private boolean equalsOrStrEquals(Object a,Object b){
        return Objects.equals(a,b)||Objects.equals(String.valueOf(a),String.valueOf(b));
    }
}
