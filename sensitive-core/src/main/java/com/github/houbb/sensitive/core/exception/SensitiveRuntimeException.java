package com.github.houbb.sensitive.core.exception;

/**
 * 脱敏运行时异常
 * @author binbin.hou
 * date 2019/1/2
 */
public class SensitiveRuntimeException extends RuntimeException {

    public SensitiveRuntimeException() {
    }

    public SensitiveRuntimeException(String message) {
        super(message);
    }

    public SensitiveRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public SensitiveRuntimeException(Throwable cause) {
        super(cause);
    }

    public SensitiveRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
