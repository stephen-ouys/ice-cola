package cn.stephen12.icecola.framework.core.infr.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;

/**
 * @author xiongliuyang
 * @version 1.0
 * @date 2022/3/17
 */
@Converter(autoApply = true)
public class LocalDateToLongConvertor implements AttributeConverter<LocalDate, Long> {

    @Override
    public Long convertToDatabaseColumn(LocalDate localDate) {
        if (localDate == null) {
            return 0L;
        }
        LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.MIN);
        return localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    @Override
    public LocalDate convertToEntityAttribute(Long sqlTimestamp) {
        if(sqlTimestamp == null || sqlTimestamp == 0L){
            return null;
        }
        return new Timestamp(sqlTimestamp).toLocalDateTime().toLocalDate();
    }
}

