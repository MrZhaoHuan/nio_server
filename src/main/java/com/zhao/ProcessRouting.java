package com.zhao;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @创建人 zhaohuan
 * @邮箱 1101006260@qq.com
 * @创建时间 2018-07-22 15:12
 * @描述
 *          1个线程 对应  2个selector(读和写)
 *          1个selector 对应  多个socket通道
 *
 *          方式1：使用已有线程，注册socket通道到selector
 *          方式2：已有线程中 socket注册过载，新建线程处理
 *
 */
@SuppressWarnings("all")
public class ProcessRouting {
    private static final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
    private static Set<SocketProcessor> socketProcessors = new CopyOnWriteArraySet<>();

    public static void execute(SocketChannel clientSocket) {
        boolean isFree = false;
        if (!socketProcessors.isEmpty()) {
            Iterator<SocketProcessor> iterator = socketProcessors.iterator();
            while (iterator.hasNext()) {
                SocketProcessor next = iterator.next();
                if (next.isFree()) {
                    isFree = true;
                    next.register(new SocketWrapper(clientSocket));
                    break;
                }
            }
        }
        if (!isFree) {
            createThreadTask(clientSocket);
        }
    }

    private static void createThreadTask(SocketChannel clientSocket) {
        try {
            SocketProcessor socketProcessor = new SocketProcessor(new SocketWrapper(clientSocket));
            socketProcessors.add(socketProcessor);
            executorService.execute(socketProcessor);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}