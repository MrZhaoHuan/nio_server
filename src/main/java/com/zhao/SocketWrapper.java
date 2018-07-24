package com.zhao;

import java.nio.channels.SocketChannel;

/**
 * @创建人 zhaohuan
 * @邮箱 1101006260@qq.com
 * @创建时间 2018-07-22 14:31
 * @描述  todo:
 */
public class SocketWrapper {
    public volatile SocketChannel socketChannel;

    public SocketWrapper(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

}