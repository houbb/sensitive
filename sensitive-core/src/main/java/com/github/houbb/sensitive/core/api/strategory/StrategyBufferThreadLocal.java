package com.github.houbb.sensitive.core.api.strategory;

/**
 * 缓存本地
 * @since 1.0.0
 */
public final class StrategyBufferThreadLocal {

    private static final ThreadLocal<StringBuilder> THREAD_LOCAL;

    static {
        THREAD_LOCAL = new ThreadLocal<>();
    }

    public static StringBuilder getBuffer() {
        StringBuilder stringBuilder = THREAD_LOCAL.get();
        if(stringBuilder == null) {
            stringBuilder = new StringBuilder();
            THREAD_LOCAL.set(stringBuilder);
        }

        return stringBuilder;
    }

    public static void clearBuffer() {
        THREAD_LOCAL.get().setLength(0);
    }

}
