package com.zhao;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @创建人 zhaohuan
 * @邮箱 1101006260@qq.com
 * @创建时间 2018-07-22 13:57
 * @描述 start()方法开启服务器
 */
@SuppressWarnings("all")
public class NioServer {
    private volatile int port = 8080;
    private volatile int backlog = 10;
    private volatile boolean isRun = false;

    public NioServer() {

    }

    public NioServer(int port, int backlog) {
        this.port = port;
        this.backlog = backlog;
    }

    public void start() {
        if (isRun) {
            throw new RuntimeException("服务器已经运行");
        }
        isRun = true;
        ServerSocketChannel server = null;
        try {
            server = ServerSocketChannel.open();
            server.bind(new InetSocketAddress(port), backlog);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        while (true) {
            try {
                SocketChannel clientSocket = server.accept();
                ProcessRouting.execute(clientSocket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}