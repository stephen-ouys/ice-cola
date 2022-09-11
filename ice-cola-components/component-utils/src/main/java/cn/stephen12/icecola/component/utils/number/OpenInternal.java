package cn.stephen12.icecola.component.utils.number;

import java.math.BigDecimal;

/**
 * 开区间
 * <br/>
 * { a < x < b }
 * <br/>
 * ------a-----------------b-----> x
 * <p>
 * <br/>
 *
 * @author ouyangsheng
 * @since 2022-05-24
 */
public class OpenInternal extends Internal {
    public OpenInternal(BigDecimal left, BigDecimal right) {
        super(left, right);
    }

    /**
     * 是否在此开区间内
     * if min < num < max
     * @param num 校验参数
     * @return 如果在此区间内 返回 true, 否则返回false
     **/
    @Override
    boolean isIn(BigDecimal num) {

        if (num == null) {
            return false;
        }
        // min < x < max
        return NumberUtil.isGreater(num, left) && NumberUtil.isLess(num, right);
    }

    @Override
    public int compareTo(Internal o) {
        return 0;
    }
}
