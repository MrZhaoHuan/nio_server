package com.zhao.message;

import com.zhao.utils.LogUtil;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @创建人 zhaohuan
 * @邮箱 1101006260@qq.com
 * @创建时间 2018-07-22 18:02
 * @描述 从SocketChannel中读取数据
 */
public class MessageReader {
    private static MessageReader messageReader;
    private final int byteSize = 4 * 1024;
    private final Map<SocketChannel, ByteBuffer> socket_data = new ConcurrentHashMap<>();

    private MessageReader() {

    }

    public ByteBuffer getBuffer(SocketChannel socketChannel) {
        return socket_data.get(socketChannel);
    }

    public static MessageReader buid() {
        return ReaderInstance.messageReader;
    }

    public void readSocketData(SocketChannel channel) throws IOException {
        ByteBuffer readBuffer = socket_data.get(channel);
        ByteBuffer buffer = ByteBuffer.allocate(byteSize);
        int count = 0;
        synchronized (channel) {
            while ((count = channel.read(buffer)) > 0) {
                if (buffer.remaining() == 0) {
                    byte[] arr = buffer.array();
                    buffer = ByteBuffer.allocate(buffer.capacity() + byteSize);
                    System.arraycopy(arr, 0, buffer.array(), 0, arr.length);
                    buffer.position(arr.length);
                }
            }
            //todo:解决读半包
            socket_data.put(channel, buffer);
            //if (null == readBuffer) {
            //    //第一次读取
            //    socket_data.put(channel, buffer);
            //} else {
            //    //说明某个链接数据分多次到达
            //    socket_data.put(channel, BufferCopy.copy(readBuffer, buffer));
            //}
            //todo:http协议判断数据是否读取完毕
            if (true) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(socket_data.get(channel).array())));
                LogUtil.info("接收到的请求行：" + bufferedReader.readLine());
            }
        }
    }

    private static class ReaderInstance {
        private static MessageReader messageReader = new MessageReader();
    }
}