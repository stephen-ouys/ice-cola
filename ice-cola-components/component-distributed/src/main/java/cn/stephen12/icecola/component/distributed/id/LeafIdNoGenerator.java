package cn.stephen12.icecola.component.distributed.id;

import cn.stephen12.icecola.framework.core.infr.id.IdNoGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

/**
 * LeafIdNo生成器
 * @author ouyangsheng
 * @date 2022-05-29
 **/
@ConditionalOnClass(IdNoGenerator.class)
@Component
public class LeafIdNoGenerator implements IdNoGenerator {

    @Autowired
    protected LeafIdService leafService;

    @Override
    public Long generateId(String tag) {
        return leafService.generateId(tag);
    }

    @Override
    public String generateNo(String tag) {
        return leafService.generateNo(tag);
    }
}
