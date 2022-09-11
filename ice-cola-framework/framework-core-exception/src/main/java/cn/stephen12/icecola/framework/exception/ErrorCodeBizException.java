package cn.stephen12.icecola.framework.exception;

import cn.stephen12.icecola.framework.model.dto.ErrorCodeEnumI;
import com.alibaba.cola.exception.BizException;
import lombok.Getter;


/**
 * 枚举错误码异常
 * @author ouyangsheng
 * @date 2022-05-26
 **/
@Getter
public class ErrorCodeBizException extends BizException {
    private final ErrorCodeEnumI codeEnum;
    private final Integer code;
    private final String msg;

    public ErrorCodeBizException(ErrorCodeEnumI codeEnum){
        super(codeEnum.getName());
        this.codeEnum = codeEnum;
        this.code = codeEnum.getCode();
        this.msg = codeEnum.getMessage();
    }

    public ErrorCodeBizException(ErrorCodeEnumI codeEnum, String msg){
        super(msg);
        this.codeEnum = codeEnum;
        this.code = codeEnum.getCode();
        this.msg = msg;
    }
}
