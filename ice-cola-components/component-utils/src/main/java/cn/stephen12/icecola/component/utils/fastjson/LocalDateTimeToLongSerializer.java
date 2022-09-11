package cn.stephen12.icecola.component.utils.fastjson;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * BigDecimal格式化
 *
 * @author jiangxinjun
 * @date 2020/06/03
 */
public class LocalDateTimeToLongSerializer implements ObjectSerializer {
    public static final LocalDateTimeToLongSerializer INSTANCE = new LocalDateTimeToLongSerializer();

    @Override
    public void write(JSONSerializer jsonSerializer, Object object, Object fieldName, Type fieldType, int features) {
        SerializeWriter out = jsonSerializer.getWriter();
        if (object == null) {
            out.writeNull();
            return;
        }
        Long timestamp = convertLocalDateTimeToLong((LocalDateTime) object);
        out.writeLong(timestamp);
    }

    private Long convertLocalDateTimeToLong(LocalDateTime localDateTime){
        return localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }
}
