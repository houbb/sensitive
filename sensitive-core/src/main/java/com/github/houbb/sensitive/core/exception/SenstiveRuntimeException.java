package com.github.houbb.sensitive.core.exception;

/**
 * 脱敏运行时异常
 * @author binbin.hou
 * @date 2019/1/2
 */
public class SenstiveRuntimeException extends RuntimeException {

    public SenstiveRuntimeException() {
    }

    public SenstiveRuntimeException(String message) {
        super(message);
    }

    public SenstiveRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public SenstiveRuntimeException(Throwable cause) {
        super(cause);
    }

    public SenstiveRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
