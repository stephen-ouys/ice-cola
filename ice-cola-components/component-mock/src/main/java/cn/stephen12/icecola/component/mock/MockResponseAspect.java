package cn.stephen12.icecola.component.mock;

import cn.stephen12.icecola.framework.model.vo.RestResult;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import uk.co.jemos.podam.api.PodamFactory;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

@Aspect
@Order(0)
public class MockResponseAspect {

    @Autowired
    private PodamFactory podamFactory;

    // todo
    // @Autowired(required = false)
    private List<Customizer> customizers;

    @Pointcut("@annotation(cn.stephen12.icecola.component.mock.Mock)")
    public void pointcut() {
    }

    @Around(value = "pointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;
        Class<?> declaringType = joinPoint.getTarget().getClass();
        Method method = joinPoint.getArgs() != null && joinPoint.getArgs().length>0
                ? declaringType.getMethod(joinPoint.getSignature().getName(),joinPoint.getArgs()[0].getClass())
                : declaringType.getMethod(joinPoint.getSignature().getName());
        Type genericReturnType = method.getGenericReturnType();
        if (genericReturnType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericReturnType;
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            result = podamFactory.manufacturePojo(method.getReturnType(), actualTypeArguments);
        }
        return adapter(result);
    }

    private Object adapter(Object result) {
        if (result instanceof RestResult) {
            RestResult restResponse = (RestResult) result;
            return RestResult.success(restResponse.getData());
        }
        return result;
    }

}
