package cn.stephen12.icecola.framework.core.infr.id;

import cn.hutool.core.util.IdUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

/**
 * 默认IdNo生成器
 *
 * @author ouyangsheng
 * @date 2022-09-14
 **/
@ConditionalOnMissingBean(IdNoGenerator.class)
public class DefaultIdNoGeneratorImpl implements IdNoGenerator{

    /**
     * 生成Id
     * <p>
     *     注意： 本方法无法生成分布式ID，如有分布式场景，建议自行实现或使用 distributed组件
     * </p>
     *
     * @param tag
     * @return
     */
    @Override
    public Long generateId(String tag){
        return IdUtil.createSnowflake(1,1).nextId();
    }

    /**
     * 生成 no
     * <p>
     *     注意： 本方法无法生成分布式编号，如有分布式场景，建议自行实现或使用 distributed组件
     * </p>
     * @param tag
     * @return
     */
    @Override
    public String generateNo(String tag){
        return IdUtil.fastUUID();
    }

}
