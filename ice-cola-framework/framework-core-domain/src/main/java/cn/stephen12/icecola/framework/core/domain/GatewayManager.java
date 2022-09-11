package cn.stephen12.icecola.framework.core.domain;

import cn.hutool.core.util.ClassUtil;
import cn.stephen12.icecola.framework.core.domain.model.BaseE;
import com.alibaba.cola.domain.ApplicationContextHelper;

import java.util.Map;
import java.util.Optional;

/**
 *  各 gateway 管理器
 *
 * @author ouyangsheng
 * @date 2022-03-29
 **/
public class GatewayManager {

    public static <T extends BaseE<T>> Optional<BaseGatewayI> findGatewayByDomainClass(Class<T> domainClass) {
        Map<String, BaseGatewayI> beans = ApplicationContextHelper.getApplicationContext().getBeansOfType(BaseGatewayI.class);
        for (BaseGatewayI gatewayBean : beans.values()) {
            Class<?> typeArgument = ClassUtil.getTypeArgument(gatewayBean.getClass(),0);
            if (domainClass.equals(typeArgument)) {
                return Optional.of(gatewayBean);
            }
        }
        return Optional.empty();
    }



}
