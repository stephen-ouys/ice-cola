package cn.stephen12.icecola.component.utils.number;

import cn.hutool.core.lang.Assert;

import java.math.BigDecimal;

/**
 * @author ouyangsheng
 * @date 2022-05-20
 **/
public class NumberUtil extends cn.hutool.core.util.NumberUtil {

    /**
     * 获取小数位数
     **/
    public static int getPrecision(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            return 0;
        }
        String string = bigDecimal.stripTrailingZeros().toPlainString();
        int index = string.indexOf(".");
        return index < 0 ? 0 : string.length() - index - 1;
    }

    /**
     * 是否在此开区间内
     * if min < x < max
     *
     * @param x   给定的值
     * @param min 开区间左端点
     * @param max 开区间右端点
     * @return
     */
    public static boolean inOpenInternal(BigDecimal x, BigDecimal min, BigDecimal max) {
        Assert.notNull(min);
        Assert.notNull(max);

        return new OpenInternal(min, max).isIn(x);
    }

    /**
     * 是否在此闭区间内
     * if min <= x <= max
     *
     * @param x   给定的值
     * @param min 开区间左端点
     * @param max 开区间右端点
     * @return
     */
    public static boolean inCloseInternal(BigDecimal x, BigDecimal min, BigDecimal max) {
        Assert.notNull(min);
        Assert.notNull(max);
        return new CloseInternal(min, max).isIn(x);
    }

    /**
     * 为空或者负数 则返回0
     *
     * @param number
     * @return
     */
    public static BigDecimal defaultZeroIfNullOrNegative(BigDecimal number) {
        if (isNullOrNegative(number)) {
            return BigDecimal.ZERO;
        }
        return number;
    }

    /**
     * 为空或者负数
     *
     * @param number
     * @return
     */
    public static boolean isNullOrNegative(BigDecimal number) {
        return number == null || NumberUtil.isLess(number, BigDecimal.ZERO);
    }

}
