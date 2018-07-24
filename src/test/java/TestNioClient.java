import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * @创建人 zhaohuan
 * @邮箱 1101006260@qq.com
 * @创建时间 2018-07-23 8:35
 * @描述
 */
public class TestNioClient {
    public static void main(String[] args) {
        for (int i = 0; i <5000; i++) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        URL url = new URL("http://localhost:8080");
                        URLConnection urlcon = url.openConnection();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(urlcon.getInputStream()));
                        System.out.println(reader.readLine());
                        reader.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }
}