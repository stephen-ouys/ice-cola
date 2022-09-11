package cn.stephen12.icecola.component.utils.period;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.stephen12.icecola.component.utils.fastjson.enhance.DeserializeInterceptor;
import cn.stephen12.icecola.component.utils.fastjson.enhance.SerializeInterceptor;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import cn.stephen12.icecola.component.utils.ClassUtil;
import cn.stephen12.icecola.component.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Json Period处理 拦截器
 *
 * @author ouyangsheng
 * @date 2022-04-29
 **/
@Slf4j
@Component
public class JsonAsPeriodInterceptor implements DeserializeInterceptor, SerializeInterceptor {
    @Override
    public boolean beforeDeserializing(DefaultJSONParser parser, Type type, Object fieldName) {
        return true;
    }

    @Override
    public Object afterDeserialized(Object result, DefaultJSONParser parser, Type type, Object fieldName) {
        if (type instanceof Class) {
            JsonAsPeriod annotation = AnnotationUtil.getAnnotation((Class) type, JsonAsPeriod.class);
            if (annotation != null) {
                if (StrUtil.isNotBlank(annotation.periodKey()) && StrUtil.isNotBlank(annotation.startField()) && StrUtil.isNotBlank(annotation.endField())) {
                    JSONObject jsonObject = JSONObject.parseObject(parser.getInput());
                    JSONArray period = jsonObject.getJSONArray(StrUtil.toUnderlineCase(annotation.periodKey()));

                    Field startField = ClassUtil.getDeclaredField((Class) type, annotation.startField());
                    Field endField = ClassUtil.getDeclaredField((Class) type, annotation.endField());

                    if (period.get(0) != null) {
                        Object startTime = null;
                        if (LocalDate.class.equals(startField.getType())) {
                            startTime = DateUtil.timestampToLocalDate(period.getLong(0));
                        }else if (LocalDateTime.class.equals(endField.getType())) {
                            startTime =  DateUtil.timestampToLocalDateTime(period.getLong(0));
                        }
                        BeanUtil.setProperty(result, annotation.startField(), startTime);
                    }
                    if (period.get(1) != null) {
                        Object endTime = null;
                        if (LocalDate.class.equals(endField.getType())) {
                            endTime = DateUtil.timestampToLocalDate(period.getLong(1));
                        }else if (LocalDateTime.class.equals(endField.getType())) {
                            endTime =  DateUtil.timestampToLocalDateTime(period.getLong(1));
                        }
                        BeanUtil.setProperty(result, annotation.endField(), endTime);
                    }
                } else {
                    log.warn("Must correctly set annotation for the class {}", type);
                }
            } else {
                log.trace("This class {} do not carry annotation @JsonAsPeriod", type);
            }
        }
        return result;
    }

    @Override
    public boolean beforeSerializing(JSONSerializer jsonSerializer, Object bean, Object fieldName, Type fieldType, int features) {
        JsonAsPeriod annotation = AnnotationUtil.getAnnotation(bean.getClass(), JsonAsPeriod.class);
        if (annotation != null) {
            if (StrUtil.isNotBlank(annotation.periodKey()) && StrUtil.isNotBlank(annotation.startField()) && StrUtil.isNotBlank(annotation.endField())) {
                Object startTime = BeanUtil.getProperty(bean, annotation.startField());
                Object endTime = BeanUtil.getProperty(bean, annotation.endField());
                if (startTime != null && endTime != null) {
                    SerializeWriter writer = jsonSerializer.getWriter();
                    writer.writeFieldName(StrUtil.toUnderlineCase(annotation.periodKey()));
                    writer.write("[");
                    writer.writeLong(toTimestamp(startTime));
                    writer.write(",");
                    writer.writeLong(toTimestamp(endTime));
                    writer.write("],");
                }
            } else {
                log.warn("Must correctly set annotation for the class {}", bean.getClass());
            }
        } else {
            log.trace("This class {} do not carry annotation @JsonAsPeriod", bean.getClass());
        }
        return true;
    }

    private Long toTimestamp(Object date) {
        if (date instanceof LocalDate) {
            return DateUtil.localDateToTimestamp((LocalDate) date);
        } else if (date instanceof LocalDateTime) {
            return DateUtil.localDateTimeToTimestamp((LocalDateTime) date);
        }
        return 0L;
    }

}
