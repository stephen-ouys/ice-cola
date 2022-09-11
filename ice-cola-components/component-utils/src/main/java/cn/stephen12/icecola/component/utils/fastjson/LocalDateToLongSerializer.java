package cn.stephen12.icecola.component.utils.fastjson;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;

/**
 * 将LocalDate序列化为时间戳
 *
 * @author ouyangsheng
 * @since  2022-03-17
 */
public class LocalDateToLongSerializer implements ObjectSerializer {
    public static final LocalDateToLongSerializer INSTANCE = new LocalDateToLongSerializer();

    @Override
    public void write(JSONSerializer jsonSerializer, Object object, Object fieldName, Type fieldType, int features) {
        SerializeWriter out = jsonSerializer.getWriter();
        if (object == null) {
            out.writeNull();
            return;
        }
        Long timestamp = convertLocalDateToLong((LocalDate) object);
        out.writeLong(timestamp);
    }


    private Long convertLocalDateToLong(LocalDate localDate){
        LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.MIN);
        return localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

}
