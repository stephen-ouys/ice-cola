package cn.stephen12.icecola.component.utils.number;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 数轴工具类
 * <p>
 *     ----[---a---]--(---b----)----(----c----]----->
 * </p>
 * @author ouyangsheng
 * @date 2022-05-24
 **/
public class NumberAxis<T extends Internal> {
    /**
     * 数轴上的所有区间
     */
    private final List<T> internals;

    public NumberAxis(){
        this.internals = new ArrayList<>();
    }

    public NumberAxis(List<T> internals){
        this.internals = internals;
    }
    /**
     * 在数轴上添加区间
     * @param internals
     */
    public void addInternals(Collection<T> internals){
        internals.addAll(internals);
    }

    /**
     * 在数轴上添加区间
     * @param internal
     */
    public void addInternal(T internal){
        internals.add(internal);
    }

    /**
     * 匹配，看该数落在哪个区间，如果未命中，则返回null
     * <p>
     *       ----[---a---]--(■■■ b ■■■)----(----c----]----->
     *   </p>
     * @param num
     * @return
     */
    public T matchInternal(BigDecimal num){
        for(T internal: internals){
            if(internal.isIn(num)){
                return internal;
            }
        }
        return null;
    }
}
