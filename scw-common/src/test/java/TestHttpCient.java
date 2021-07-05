import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.runner.RunWith;

import java.io.IOException;


public class TestHttpCient {

    public static void main(String[] args) throws IOException {
        //1、创建httpclient对象
        DefaultHttpClient httpClient = new DefaultHttpClient();

        //2、创建请求对象 get、post
        HttpGet httpGet = new HttpGet("http://www.ujiuye.com");

        //3、发出请求
        HttpResponse response = httpClient.execute(httpGet);

        //4、获取响应结果
        HttpEntity entity = response.getEntity();

        //获取响应状态码
        int statusCode = response.getStatusLine().getStatusCode();

        //判断状态码 200
        if(statusCode==200){
            String s = EntityUtils.toString(entity, "GBK");
            System.out.println("响应结果:"+s);
        }
    }
}
