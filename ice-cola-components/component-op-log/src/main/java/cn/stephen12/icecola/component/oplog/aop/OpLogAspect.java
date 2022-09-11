package cn.stephen12.icecola.component.oplog.aop;

import cn.stephen12.icecola.component.oplog.OpLogModel;
import cn.stephen12.icecola.component.oplog.spi.OpLogListener;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import java.util.List;

/**
 * //TODO 要注意环绕通知要在事务外
 * 操作日志AOP
 * @author ouyangsheng
 * @since 2022-08-11
 */
@Aspect
@Order(0)
public class OpLogAspect {
    @Autowired
    private List<OpLogListener> opLogListeners;

    @Pointcut("@annotation(cn.stephen12.icecola.component.oplog.aop.OpLog)")
    public void pointcut() {
    }

    @Around(value = "pointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        OpLogModel opLogModel = new OpLogModel();
        Object[] args = joinPoint.getArgs();
        opLogModel.setParam(args.toString());
        //TODO 获取ID
        opLogModel.setBizId(null);
        try {
            Object result = joinPoint.proceed();
            //TODO 结果ToString
            opLogModel.setFeedback(result.toString());

            log(opLogModel);
            return result;
        }catch (Throwable e){
            handleError(opLogModel,e);
            throw e;
        }
    }

    private void log(OpLogModel opLogModel){
        for(OpLogListener logListener: opLogListeners){
            logListener.onLog(opLogModel,null);
        }
    }

    private void handleError(OpLogModel opLogModel,Throwable e){
        for(OpLogListener logListener: opLogListeners){
            logListener.onLog(opLogModel,e);
        }
    }



}
