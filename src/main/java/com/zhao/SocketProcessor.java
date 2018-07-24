package com.zhao;

import com.zhao.message.MessageReader;
import com.zhao.message.MessageWriter;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @创建人 zhaohuan
 * @邮箱 1101006260@qq.com
 * @创建时间 2018-07-22 14:37
 * @描述 从阻塞队列中拿socket，读数据，写数据
 */
public class SocketProcessor implements Runnable {
    private volatile Selector readSelector;
    private volatile Selector writeSelector;
    private final ArrayBlockingQueue<SocketWrapper> clientSocketQueue = new ArrayBlockingQueue(1024);

    public SocketProcessor(SocketWrapper socketWrapper) throws IOException, InterruptedException {
        addSocket(socketWrapper);
        readSelector = Selector.open();
        writeSelector = Selector.open();
    }

    @Override
    public void run() {
        while (true) {
            try {
                pollSocketToSelector();
                readSocket();
                writeSocket();
                Thread.sleep(20);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void writeSocket() {
        int count = 0;
        try {
            count = writeSelector.selectNow();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        if (count > 0) {
            Set<SelectionKey> selectionKeys = writeSelector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey next = iterator.next();
                SocketChannel channel = (SocketChannel) next.channel();
                try {
                    MessageWriter.buid().writeSocketData(channel);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    iterator.remove();
                    next.cancel();
                    try {
                        next.channel().close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void readSocket() {
        int count = 0;
        try {
            count = readSelector.selectNow();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        if (count > 0) {
            Set<SelectionKey> selectionKeys = readSelector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey next = iterator.next();
                SocketChannel channel = (SocketChannel) next.channel();
                try {
                    MessageReader.buid().readSocketData(channel);
                    channel.register(writeSelector, SelectionKey.OP_WRITE);
                } catch (IOException e) {
                    e.printStackTrace();
                    try {
                        channel.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                } finally {
                    iterator.remove();
                    next.cancel();
                }
            }
        }

    }

    private void pollSocketToSelector() {
        SocketWrapper socketWrapper = clientSocketQueue.poll();
        while (null != socketWrapper) {
            try {
                socketWrapper.socketChannel.configureBlocking(false);
                socketWrapper.socketChannel.register(readSelector, SelectionKey.OP_READ);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                socketWrapper = clientSocketQueue.poll();
            }
        }
    }


    public boolean isFree() {
        return clientSocketQueue.size() < 1024;
    }

    private void addSocket(SocketWrapper clientSocket) throws InterruptedException {
        clientSocketQueue.offer(clientSocket, 1, TimeUnit.SECONDS);
    }

    public void register(SocketWrapper socketWrapper) {
        try {
            addSocket(socketWrapper);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}