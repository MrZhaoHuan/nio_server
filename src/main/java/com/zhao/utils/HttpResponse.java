package com.zhao.utils;

import java.nio.charset.Charset;

/**
 * @创建人 zhaohuan
 * @邮箱 1101006260@qq.com
 * @创建时间 2018-07-22 20:48
 * @描述  构造响应行和响应头
 */
public class HttpResponse {
    private static final String RESPONSE_LINE = "HTTP/1.1 200 ok";
    private static final String CONTENT_TYPE = "text/html;charset=" + Charset.defaultCharset();
    private final int bodyLength;

    public HttpResponse(int bodyLength) {
        this.bodyLength = bodyLength;
    }

    public byte[] getResponseHeadr() {
        StringBuilder header = new StringBuilder();
        header.append(RESPONSE_LINE).append(System.lineSeparator())
                .append("Content-Length: ").append(bodyLength).append(System.lineSeparator())
                .append("Content-Type: ").append(CONTENT_TYPE).append(System.lineSeparator()).append(System.lineSeparator());

        return header.toString().getBytes();
    }
}