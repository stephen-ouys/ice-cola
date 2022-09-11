package cn.stephen12.icecola.framework.model.vo;

import cn.stephen12.icecola.framework.model.dto.ErrorCodeEnumI;
import com.alibaba.cola.exception.BizException;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.function.Supplier;

/**
 * 参考 Optional 对响应结果进行包装
 *
 * @author ouyangsheng
 * @date 2022-05-26
 **/
@Data
@Builder
public class RestResult<T> implements Serializable {
    /**
     * 成功的状态码
     */
    private final static Integer SUCCESS_CODE_0 = 0;
    private final static Integer SUCCESS_CODE_200 = 200;
    private final static String DEFAULT_SUCCESS_MESSAGE = "OK";

    private static final long serialVersionUID = 6095433538316185017L;
    private int code;
    private String message;
    private T data;

    public RestResult() {
    }

    public RestResult(int code, String message, T data) {
        this.code = code;
        this.setMessage(message);
        this.data = data;
    }

    public RestResult(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public RestResult(int code, String message) {
        this.code = code;
        this.setMessage(message);
    }

    public boolean isOk() {
        return this.code == SUCCESS_CODE_0 || this.code == SUCCESS_CODE_200;
    }

    @Override
    public String toString() {
        return "RestResult{code=" + this.code + ", message='" + this.message + '\'' + ", data=" + this.data + '}';
    }

    public static RestResult fail(int code, String message) {
        return new RestResult(code, message);
    }

    public static RestResult success() {
        return new RestResult(SUCCESS_CODE_0, DEFAULT_SUCCESS_MESSAGE);
    }

    public static <T> RestResult<T> success(T data) {
        return new RestResult(SUCCESS_CODE_0, DEFAULT_SUCCESS_MESSAGE, data);
    }

    public static <T> RestResult<T> success(String message, T data) {
        return new RestResult(SUCCESS_CODE_0, message, data);
    }

    /**
     * 如果未成功，则抛异常
     *
     * @param ex
     * @return
     */
    public T elseThrow(Supplier<RuntimeException> ex) {
        if (this.isOk()) {
            return this.getData();
        }
        throw ex.get();
    }

    /**
     * 如果失败，抛出异常
     *
     * @param codeEnum
     * @param errorMsgTemplate
     * @param params
     * @return
     */
    public T elseThrow(ErrorCodeEnumI codeEnum, String errorMsgTemplate, Object... params) {
        return elseThrow(() -> {
            String message = String.format(errorMsgTemplate, params);
            return new BizException(String.valueOf(codeEnum.getCode()), message);
        });
    }

    /**
     * 如果失败，抛出异常
     *
     * @return
     */
    public T elseThrow() {
        return elseThrow(() -> new BizException(String.valueOf(this.getCode()), this.getMessage())
        );
    }
}
