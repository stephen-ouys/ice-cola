package cn.stephen12.icecola.component.utils.period;

import cn.hutool.core.collection.CollectionUtil;
import cn.stephen12.icecola.component.utils.DateUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 周期 Client Object
 *
 * @author ouyangsheng
 * @since 2022-03-14
 */
@ApiModel(value = "PeriodCO", description = "周期 Client Object")
@Data
@NoArgsConstructor
public class PeriodCO {

    private static final Integer BEGIN_DATE_INDEX = 0;

    private static final Integer END_DATE_INDEX = 1;

    @ApiModelProperty("日期范围")
    @Size(max = 2, min = 2)
    private List<LocalDate> between;

    public PeriodCO(List<Long> timestampBetween) {
        between = new ArrayList<>(2);
        between.add(BEGIN_DATE_INDEX, DateUtil.timestampToLocalDate(timestampBetween.get(BEGIN_DATE_INDEX)));
        between.add(END_DATE_INDEX, DateUtil.timestampToLocalDate(timestampBetween.get(END_DATE_INDEX)));
    }

    /**
     * 开始日期
     *
     * @return
     */
    public LocalDateTime getBeginDate() {
        if (!CollectionUtil.isEmpty(between)) {
            return LocalDateTime.of(between.get(BEGIN_DATE_INDEX), LocalTime.MIN);
        }
        return null;
    }

    /**
     * 结束日期
     *
     * @return
     */
    public LocalDateTime getEndDate() {
        if (!CollectionUtil.isEmpty(between) && between.size() == 2) {
            return LocalDateTime.of(between.get(END_DATE_INDEX), LocalTime.MAX);
        }
        return null;
    }

    /**
     * 是否为空
     * @return
     */
    public boolean isEmpty() {
        return getBeginDate() != null && getEndDate() != null;
    }
}
