package cn.stephen12.icecola.framework.exception;

import cn.stephen12.icecola.framework.model.vo.RestResult;
import com.alibaba.cola.exception.BizException;
import com.alibaba.cola.exception.SysException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 核心异常处理
 *
 * @author ouyangsheng
 * @date 2022-05-26
 **/
@Slf4j
@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE - 10)
public class CoreExceptionHandler {
    private static final Integer DEFAULT_CLIENT_ERROR_CODE = 400;
    private static final Integer DEFAULT_SERVER_ERROR_CODE = 500;

    /**
     * 业务异常，抛给用户看到，不需要打堆栈
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = ErrorCodeBizException.class)
    @ResponseBody
    public RestResult<?> handleErrorCodeBizException(ErrorCodeBizException e) {
        log.info("ErrorCodeBizException( code: {}, message: {})", e.getCode(), e.getMessage());
        return RestResult.fail(e.getCode(), e.getMessage());
    }

    /**
     * 业务异常，抛给用户看到，不需要打堆栈
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = BizException.class)
    @ResponseBody
    public RestResult<?> handleBizException(BizException e) {
        log.info("BizException( code: {}, message: {})", e.getErrCode(), e.getMessage());
        return RestResult.fail(DEFAULT_CLIENT_ERROR_CODE, e.getMessage());
    }

    /**
     * 参数错误，要打堆栈
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseBody
    public RestResult<?> handleIllegalArgumentException(IllegalArgumentException e) {
        log.info("IllegalArgumentException( message: {}), cause: {}", e.getMessage(), e);
        return RestResult.fail(DEFAULT_CLIENT_ERROR_CODE
                , e.getMessage());
    }

    /**
     * 状态错误，需要打堆栈
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = IllegalStateException.class)
    @ResponseBody
    public RestResult<?> handleIllegalStateException(IllegalStateException e) {
        log.warn("IllegalStateException( message: {}), cause: {}", e.getMessage(), e);
        return RestResult.fail(DEFAULT_CLIENT_ERROR_CODE
                , e.getMessage());
    }

    /**
     * 系统异常，抛给程序员看的，要打堆栈
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = SysException.class)
    @ResponseBody
    public RestResult<?> handleSysException(SysException e) {
        log.error("SysException( code: {}, message: {}), cause: {}", e.getErrCode(), e.getMessage(), e);
        return RestResult.fail(DEFAULT_SERVER_ERROR_CODE
                , "内部系统错误");
    }


}
