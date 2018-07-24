import com.zhao.NioServer;

import java.io.IOException;

/**
 * @创建人 zhaohuan
 * @邮箱 1101006260@qq.com
 * @创建时间 2018-07-22 16:39
 * @描述
 */
public class TestNioServer {
    public static void main(String[] args) throws IOException {
        NioServer server = new NioServer();
        server.start();
    }
}