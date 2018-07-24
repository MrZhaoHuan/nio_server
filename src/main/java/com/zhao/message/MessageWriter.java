package com.zhao.message;

import com.zhao.utils.HttpResponse;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @创建人 zhaohuan
 * @邮箱 1101006260@qq.com
 * @创建时间 2018-07-22 18:02
 * @描述  往SocketChannel中写数据
 */
public class MessageWriter {
    private MessageWriter() {

    }

    public static MessageWriter buid() {
        return WriterInstance.messageWriter;
    }

    public void writeSocketData(SocketChannel channel) throws IOException {
        ByteBuffer bodyBuffer = MessageReader.buid().getBuffer(channel);
        bodyBuffer.clear();

        HttpResponse response = new HttpResponse(bodyBuffer.array().length);
        byte[] responseHeadrByte = response.getResponseHeadr();
        ByteBuffer headerBuffer = ByteBuffer.wrap(responseHeadrByte);

        while (headerBuffer.hasRemaining()) {
            channel.write(headerBuffer);
        }
        while (bodyBuffer.hasRemaining()) {
            channel.write(bodyBuffer);
        }
    }

    private static class WriterInstance {
        private static MessageWriter messageWriter = new MessageWriter();
    }

}