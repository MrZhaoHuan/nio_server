package com.zhao.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @创建人 zhaohuan
 * @邮箱 1101006260@qq.com
 * @创建时间 2018-07-03 14:51
 * @描述   日志打印
 */
public class LogUtil {
    private static final Logger logger = LoggerFactory.getLogger(LogUtil.class);

    /**
     * @描述 记录错误消息
     * @参数 [message]
     * @返回值 void
     */
    public static void error(String message) {
        logger.error(message);
    }

    /**
     * @描述 记录错误消息, 打印堆栈异常信息
     * @参数 [message, t]
     * @返回值 void
     */
    public static void error(String message, Throwable t) {
        logger.error(message, t);
    }

    /**
     * @描述 记录普通消息
     * @参数 [message]
     * @返回值 void
     */
    public static void info(String message) {
        logger.info(message);
    }

}