package cn.stephen12.icecola.component.utils.fastjson;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * 将时间戳反序列化为LocalDateTime
 *
 * @author ouyangsheng
 * @date 2022-03-17
 */
public class LongToLocalDateTimeDeserializer implements ObjectDeserializer {
    public static final LongToLocalDateTimeDeserializer INSTANCE = new LongToLocalDateTimeDeserializer();

    @Override
    public LocalDateTime deserialze(DefaultJSONParser defaultJSONParser, Type type, Object o) {
        Long timestamp = defaultJSONParser.parseObject(Long.class);
        if (timestamp == null) {
            return null;
        }
        return convertLongToLocalDateTime(timestamp);
    }

    @Override
    public int getFastMatchToken() {
        return 0;
    }

    private LocalDateTime convertLongToLocalDateTime(Long timestamp) {
        return LocalDateTime.ofEpochSecond(timestamp / 1000, 0, ZoneOffset.ofHours(8));
    }
}
