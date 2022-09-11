package cn.stephen12.icecola.framework.core.infr.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @author xiongliuyang
 * @version 1.0
 * @date 2022/3/17
 */
@Converter(autoApply = true)
public class LocalDateTimeToLongConvertor implements AttributeConverter<LocalDateTime, Long> {

    @Override
    public Long convertToDatabaseColumn(LocalDateTime locDateTime) {
        if (locDateTime == null) {
            return 0L;
        }
        return locDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Long sqlTimestamp) {
        if (sqlTimestamp == null || sqlTimestamp == 0L) {
            return null;
        }
        return new Timestamp(sqlTimestamp).toLocalDateTime();
    }
}

