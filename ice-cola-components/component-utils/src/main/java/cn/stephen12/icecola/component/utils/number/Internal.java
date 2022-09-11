package cn.stephen12.icecola.component.utils.number;

import java.math.BigDecimal;

/**
 * 区间的抽象类
 * @author ouyangsheng
 * @date 2022-05-24
 **/
abstract class Internal implements Comparable<Internal> {

    protected BigDecimal left;
    protected BigDecimal right;

    public Internal(BigDecimal left, BigDecimal right) {
        this.left = left;
        this.right = right;
    }

    /**
     * 某一个数是否在本区间内
     *
     * @param num
     * @return
     */
    abstract boolean isIn(BigDecimal num);
}
