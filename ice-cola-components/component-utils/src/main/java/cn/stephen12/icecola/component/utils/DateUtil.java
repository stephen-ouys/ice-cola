package cn.stephen12.icecola.component.utils;

import com.google.common.base.Throwables;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.time.DateUtils;

import java.sql.Timestamp;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;

/**
 * java8日期与时间操作工具类
 *
 * @author zhanghao
 */
@SuppressWarnings({"WeakerAccess", "unused"})
@UtilityClass
public class DateUtil extends cn.hutool.core.date.DateUtil {

    /**
     * 默认的时间日期样式
     */
    @SuppressWarnings("WeakerAccess")
    public final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期时间数字样式
     */
    public final String DATE_TIME_NUMBER_FORMAT = "yyyyMMddHHmmssSSS";

    /**
     * 日期样式
     */
    public final DateTimeFormatter DATE_NUMBER_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    /**
     * 日期样式
     */
    public final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    /**
     * 标准日期样式
     */
    public final DateTimeFormatter DATE_FORMATTER_MINUTE = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * 标准日期样式
     */
    public final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(STANDARD_FORMAT);

    /**
     * 中国时区
     */
    public final String CN_ZONE_ID = "Asia/Shanghai";

    /**
     * 获取当前标准时间
     */
    public String now() {
        return localDateTimeToFormatStr(LocalDateTime.now(), STANDARD_FORMAT);
    }

    /**
     * 获取当前标准时间
     */
    public String nowWithFormat(String format) {
        return localDateTimeToFormatStr(LocalDateTime.now(), format);
    }

    /**
     * Date 转 LocalDateTime
     *
     * @param date .
     * @return LocalDateTime
     */
    public LocalDateTime dateToLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        long nanoOfSecond = (date.getTime() % 1000) * 1000000;
        return LocalDateTime.ofEpochSecond(date.getTime() / 1000, (int) nanoOfSecond, ZoneOffset.of("+8"));
    }

    /**
     * LocalDateTime 转  Date
     *
     * @param localDateTime .
     * @return Date
     */
    public Date localDateTimeToDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Date 转 LocalDate
     *
     * @param date .
     * @return LocalDate
     */
    public LocalDate dateToLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        return dateToLocalDateTime(date).toLocalDate();
    }

    /**
     * LocalDate 转 Date
     *
     * @param localDate .
     * @return Date
     */
    public Date localDateToDate(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        return Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Timestamp 转 LocalDateTime
     *
     * @param date .
     * @return LocalDateTime
     */
    public LocalDateTime timestampToLocalDateTime(Timestamp date) {
        if (date == null) {
            return null;
        }
        return LocalDateTime.ofEpochSecond(date.getTime() / 1000, date.getNanos(), ZoneOffset.of("+8"));
    }

    /**
     * Long 转 LocalDateTime
     *
     * @param timestamp .
     * @return LocalDateTime
     */
    public LocalDateTime timestampToLocalDateTime(Long timestamp) {
        if (timestamp == null) {
            return null;
        }
        return LocalDateTime.ofEpochSecond(timestamp / 1000, 0, ZoneOffset.of("+8"));
    }

    /**
     * Timestamp 转 LocalDate
     *
     * @param date .
     * @return LocalDate
     */
    public LocalDate timestampToLocalDate(Timestamp date) {
        if (date == null) {
            return null;
        }
        return timestampToLocalDateTime(date).toLocalDate();
    }

    /**
     * Long 转 LocalDate
     *
     * @param timestamp .
     * @return LocalDate
     */
    public LocalDate timestampToLocalDate(Long timestamp) {
        if (timestamp == null) {
            return null;
        }
        return timestampToLocalDateTime(timestamp).toLocalDate();
    }

    /**
     * localDateTime转自定义格式string
     *
     * @param localDateTime LocalDateTime对象
     * @param format        指定转换的格式，如：yyyy-MM-dd hh:mm:ss
     * @return .
     */
    @SuppressWarnings({"deprecation"})
    public String localDateTimeToFormatStr(LocalDateTime localDateTime, String format) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return localDateTime.format(formatter);
        } catch (DateTimeParseException e) {
            Throwables.propagate(e);
        }
        return null;
    }

    /**
     * 时间戳转自定义格式string
     *
     * @param timestamp 时间戳
     * @return .
     */
    public String timestampToFormatStr(long timestamp, DateTimeFormatter dateTimeFormatter) {
        return dateTimeFormatter.format(timestampToDatetime(timestamp));
    }

    /**
     * @param temporal          .
     * @param dateTimeFormatter .
     * @return .
     */
    public String formatStr(TemporalAccessor temporal, DateTimeFormatter dateTimeFormatter) {
        return dateTimeFormatter.format(temporal);
    }

    /**
     * 时间戳转自定义格式string
     *
     * @param timestamp 时间戳
     * @return .
     */
    public String timestampToDateNumberFormatStr(long timestamp) {
        return timestampToFormatStr(timestamp, DATE_NUMBER_FORMATTER);
    }

    /**
     * 转成数字日期格式字符串
     *
     * @param localDate .
     * @return .
     */
    public String localDateToDateNumberFormatStr(LocalDate localDate) {
        return formatStr(localDate, DATE_NUMBER_FORMATTER);
    }

    /**
     * 时间戳转自定义格式string
     *
     * @param timestamp 时间戳
     * @return .
     */
    public String timestampToDateFormatStr(long timestamp) {
        return timestampToFormatStr(timestamp, DATE_FORMATTER);
    }

    /**
     * 时间戳转自定义格式string
     *
     * @param timestamp 时间戳
     * @return .
     */
    public String timestampToDateTimeFormatStr(long timestamp) {
        return timestampToFormatStr(timestamp, DATE_TIME_FORMATTER);
    }

    /**
     * 时间戳转自定义格式string
     *
     * @param timestamp 时间戳
     * @return .
     */
    public String timestampToDateStandardFormatStr(long timestamp) {
        return DATE_FORMATTER_MINUTE.format(timestampToDatetime(timestamp));
    }

    /**
     * string 转 LocalDateTime
     *
     * @param dateStr 例："2017-08-11 01:00:00"
     * @param format  例："yyyy-MM-dd HH:mm:ss"
     * @return .
     */
    @SuppressWarnings({"deprecation"})
    public LocalDateTime stringToLocalDateTime(String dateStr, String format) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return LocalDateTime.parse(dateStr, formatter);
        } catch (DateTimeParseException e) {
            Throwables.propagate(e);
        }
        return null;
    }

    /**
     * 获取指定日期的天数
     *
     * @param date 指定的日期
     * @return Integer 天数
     */
    public Integer getDays(Date date) {
        if (date == null) {
            return null;
        }
        return dateToLocalDateTime(date).getMonth().length(dateToLocalDate(date).isLeapYear());
    }

    /**
     * 获取指定日期的数字格式的星期
     *
     * @param date .
     * @return int 数字格式的星期
     */
    public Integer getWeek(Date date) {
        if (date == null) {
            return null;
        }
        return dateToLocalDateTime(date).getDayOfWeek().getValue();
    }

    /**
     * 获取指定日期的中文格式的星期
     *
     * @param date .
     * @return String 中文格式的星期，如：星期一
     */
    public String getWeekStr(Date date) {
        if (date == null) {
            return null;
        }
        String[] weekDays = {"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"};
        int week = dateToLocalDateTime(date).getDayOfWeek().getValue();
        return weekDays[week - 1];
    }

    /**
     * 计算两个日期相差的天数,相同日期为0天
     *
     * @param before 前一日期
     * @param after  后一日期
     * @return int 天数
     */
    public Integer getDiffDay(Date before, Date after) {
        if (before == null || after == null) {
            return null;
        }
        long days = dateToLocalDate(before).until(dateToLocalDate(after), ChronoUnit.DAYS);
        return (int) days;
    }

    /**
     * 计算两个日期相差的天数,相同日期算一天
     *
     * @param before 前一日期
     * @param after  后一日期
     * @return int 天数
     */
    public Integer getDiffDays(Date before, Date after) {
        if (before == null || after == null) {
            return null;
        }
        Date start = DateUtils.truncate(before, Calendar.DATE);
        Date end = DateUtils.truncate(after, Calendar.DATE);
        if (end.getTime() - start.getTime() == 0) {
            return 1;
        }
        long days = dateToLocalDate(start).until(dateToLocalDate(end), ChronoUnit.DAYS);
        return (int) days + 1;
    }

    /**
     * 计算两个日期相差的月数
     *
     * @param before 前一日期
     * @param after  后一日期
     * @return int 月数
     */
    public Integer getDiffMonths(Date before, Date after) {
        if (before == null || after == null) {
            return null;
        }
        long months = dateToLocalDate(before).until(dateToLocalDate(after), ChronoUnit.MONTHS);
        return (int) months;
    }

    /**
     * 计算两个日期相差的年数
     *
     * @param before 前一日期
     * @param after  后一日期
     * @return int 年数
     */
    public Integer getDiffYears(Date before, Date after) {
        if (before == null || after == null) {
            return null;
        }
        long years = dateToLocalDate(before).until(dateToLocalDate(after), ChronoUnit.YEARS);
        return (int) years;
    }

    /**
     * 增加或减少年/月/周/天/小时/分/秒数
     *
     * @param date       指定的日期
     * @param chronoUnit 年/月/周/天/小时/分/秒数，如：ChronoUnit.DAYS 指的是天
     * @param num        增加或减少的数，如：增加2 或 减少-2
     * @return Date
     */
    public Date addOrSubDate(Date date, ChronoUnit chronoUnit, int num) {
        if (date == null) {
            return null;
        }
        LocalDateTime localDateTime = dateToLocalDateTime(date).plus(num, chronoUnit);
        return localDateTimeToDate(localDateTime);
    }

    /**
     * 判断当前时间是否在指定的时间范围内(包含时分秒)
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return boolean
     */
    public Boolean isTimeInRange(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return false;
        }
        LocalDateTime now = LocalDateTime.now(Clock.system(ZoneId.of(CN_ZONE_ID)));
        LocalDateTime start = dateToLocalDateTime(startDate);
        LocalDateTime end = dateToLocalDateTime(endDate);
        return (start.isBefore(now) && end.isAfter(now)) || start.isEqual(now) || end.isEqual(now);
    }

    /**
     * 判断当前时间是否在指定的时间范围内(不含时分秒)
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return boolean
     */
    public Boolean isDateInRange(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return false;
        }
        Date now = DateUtils.truncate(new Date(), Calendar.DATE);
        Date start = DateUtils.truncate(startDate, Calendar.DATE);
        Date end = DateUtils.truncate(endDate, Calendar.DATE);
        return now.getTime() - start.getTime() >= 0 && end.getTime() - now.getTime() >= 0;
    }

    /**
     * 判断当前时间是否在指定的时间之前(包含时分秒)
     *
     * @param date 指定时间
     * @return boolean
     */
    public Boolean isTimeInBefore(Date date) {
        if (date == null) {
            return false;
        }
        LocalDateTime now = LocalDateTime.now(Clock.system(ZoneId.of(CN_ZONE_ID)));
        LocalDateTime beforeDate = dateToLocalDateTime(date);
        return now.isBefore(beforeDate);
    }

    /**
     * 判断当前时间是否在指定的时间之前(不含时分秒)
     *
     * @param date 指定时间
     * @return boolean
     */
    public Boolean isDateInBefore(Date date) {
        if (date == null) {
            return false;
        }
        Date now = DateUtils.truncate(new Date(), Calendar.DATE);
        Date beforeDate = DateUtils.truncate(date, Calendar.DATE);
        return beforeDate.getTime() - now.getTime() > 0;
    }

    /**
     * 判断当前时间是否在指定的时间之后(包含时分秒)
     *
     * @param date 指定时间
     * @return boolean
     */
    public Boolean isTimeInAfter(Date date) {
        if (date == null) {
            return false;
        }
        LocalDateTime now = LocalDateTime.now(Clock.system(ZoneId.of(CN_ZONE_ID)));
        LocalDateTime afterDate = dateToLocalDateTime(date);
        return now.isAfter(afterDate);
    }

    /**
     * 判断当前时间是否在指定的时间之后(不含时分秒)
     *
     * @param date 指定时间
     * @return boolean
     */
    public Boolean isDateInAfter(Date date) {
        if (date == null) {
            return false;
        }
        Date now = DateUtils.truncate(new Date(), Calendar.DATE);
        Date afterDate = DateUtils.truncate(date, Calendar.DATE);
        return now.getTime() - afterDate.getTime() > 0;
    }

    /**
     * 获得当天日期最小时间
     *
     * @return .
     */
    public LocalDateTime getStartOfDay() {
        return LocalDateTime.now().with(LocalTime.MIN);
    }

    /**
     * 获得当天日期最小时间
     *
     * @return .
     */
    public long getStartOfDayWithTimestamp() {
        return localDateTimeToTimestamp(getStartOfDay());
    }

    /**
     * 获得当天日期最小时间
     *
     * @return .
     */
    public long getStartOfDayWithTimestamp(LocalDate date) {
        LocalDateTime localDateTime = date.atStartOfDay();
        return localDateTimeToTimestamp(localDateTime);
    }

    /**
     * 获取指定时间的当前日期的最小时间
     *
     * @return .
     */
    public long getStartOfDayWithTimestamp(LocalDateTime localDateTime) {
        return localDateTimeToTimestamp(localDateTime.with(LocalTime.MIN));
    }

    /**
     * 获得指定日期前一天
     *
     * @return .
     */
    public long getYesterdayWithTimestamp(Long date) {
        long ms = date - 1 * 24 * 3600 * 1000L;
        return ms;
    }

    /**
     * long类型转LocalDate
     *
     * @return .
     */
    public LocalDate turnLongToLocalDate(Long date) {
        long turnDate = date / 1000;
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(turnDate), ZoneId.systemDefault());
        LocalDate localDate = localDateTime.toLocalDate();
        return localDate;
    }

    /**
     * 获得指定日期最小时间 :<br/>
     * 如：传入的日期为：2018-10-10 10:10:10，处理后为：2018-10-10 00:00:00
     *
     * @param date 指定日期
     * @return .
     */
    public Date getStartOfDay(Date date) {
        return new Date(getStartOfDay(date.getTime()));
    }

    /**
     * 获得当天日期最大时间
     *
     * @return .
     */
    public LocalDateTime getEndOfDay() {
        return LocalDateTime.now().with(LocalTime.MAX);
    }

    /**
     * 获得指定日期最小时间的毫秒数 :<br/>
     * 如：传入的日期为：2018-10-10 10:10:10，处理后为：2018-10-10 00:00:00
     *
     * @param timestamp 指定日期的毫秒数
     * @return .
     */
    public long getStartOfDay(long timestamp) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        return startOfDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * 获得指定日期最大时间的毫秒数 :<br/>
     * 如：传入的日期为：2018-10-10 10:10:10，处理后为：2018-10-10 11:59:59
     *
     * @param timestamp 指定日期的毫秒数
     * @return .
     */
    public long getEndOfDay(long timestamp) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
        return endOfDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * 获得指定日期最大时间 :<br/>
     * 如：传入的日期为：2018-10-10 10:10:10，处理后为：2018-10-10 23:59:59
     *
     * @param date 指定日期
     * @return .
     */
    public Date getEndOfDay(Date date) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
        LocalDateTime endOfDayMax = localDateTime.with(LocalTime.MAX);
        LocalDateTime endOfDay = endOfDayMax.with(ChronoField.NANO_OF_SECOND, 0);
        return Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获得当天日期最大时间戳
     *
     * @return .
     */
    public long getEndOfDayWithTimestamp() {
        return localDateTimeToTimestamp(getEndOfDay());
    }

    /**
     * 获得当天日期最大时间戳
     *
     * @return .
     */
    public long getEndOfDayWithTimestamp(LocalDate date) {
        LocalDateTime localDateTime = date.atStartOfDay().with(LocalTime.MAX);
        return localDateTimeToTimestamp(localDateTime);
    }

    /**
     * 获取指定时间的当前日期的最小时间
     *
     * @return .
     */
    public long getEndOfDayWithTimestamp(LocalDateTime localDateTime) {
        return localDateTimeToTimestamp(localDateTime.with(LocalTime.MAX));
    }

    /**
     * 毫秒数转时间
     *
     * @param timestamp 1970-01-01的毫秒数
     * @return .
     */
    public LocalDateTime timestampToDatetime(Long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        return LocalDateTime.ofInstant(instant, ZoneId.of(CN_ZONE_ID));
    }

    /**
     * 日期比较
     *
     * @param date1 .
     * @param date2 .
     * @return .
     */
    public int compare(String date1, String date2) {
        return LocalDate.parse(date1).compareTo(LocalDate.parse(date2));
    }

    /**
     * 时间比较（1970-01-01到现在的毫秒数）
     *
     * @param epochDay1 毫秒数1
     * @param epochDay2 毫秒数2
     * @return .
     */
    public int compare(Long epochDay1, Long epochDay2) {
        return timestampToDatetime(epochDay1).compareTo(timestampToDatetime(epochDay2));
    }

    /**
     * 时间转时间戳
     *
     * @param localDateTime 时间
     * @return 时间戳
     */
    public long localDateTimeToTimestamp(LocalDateTime localDateTime) {
        return localDateTime.toInstant(ZoneId.of(CN_ZONE_ID).getRules().getOffset(localDateTime)).toEpochMilli();
    }

    /**
     * 时间转时间戳
     *
     * @param localDate 日期
     * @return 时间戳
     */
    public long localDateToTimestamp(LocalDate localDate) {
        LocalDateTime localDateTime = localDate.atStartOfDay();
        return localDateTimeToTimestamp(localDateTime);
    }


    /**
     * 是否在这个周期内，默认 beginning <= oneDay <= end
     *
     * @param oneDay
     * @param beginning
     * @param end
     * @return
     */
    public boolean isIn(LocalDate oneDay, LocalDate beginning, LocalDate end) {
        if (beginning == null || end == null) {
            return false;
        }

        boolean isAfterOrOnBeginning = oneDay.isAfter(beginning) || oneDay.isEqual(beginning);
        boolean isBeforeOrOnEnd = oneDay.isBefore(end) || oneDay.equals(end);

        return isAfterOrOnBeginning && isBeforeOrOnEnd;
    }
}
