package com.zhao.utils;

import java.nio.ByteBuffer;

/**
 * @创建人 zhaohuan
 * @邮箱 1101006260@qq.com
 * @创建时间 2018-07-22 14:08
 * @描述  合并2个ByteBuffer
 */
public class BufferCopy {
    public static ByteBuffer copy(ByteBuffer oldBuffer, ByteBuffer newBuffer) {
        ByteBuffer resultBuffer = null;
        byte[] oldArr = oldBuffer.array();
        byte[] newArr = newBuffer.array();
        int totalSize = oldArr.length + newArr.length;

        resultBuffer = ByteBuffer.allocate(totalSize);
        resultBuffer.put(oldArr).put(newArr);
        return resultBuffer;
    }
}