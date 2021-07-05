import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class TestOss {

    public static void main(String[] args) throws FileNotFoundException {
        String AccessKeyId="LTAI4G7okgmM99FHmrxizdRu";
        String AccessKeySecret="aSHoxLMBjDfS0AGFSeb5nsLmJIYTwf";

        String endpoint="oss-cn-guangzhou.aliyuncs.com";
        //创建oss客户端对象
        OSS ossClient = new OSSClientBuilder().build(endpoint, AccessKeyId, AccessKeySecret);

        //读取本地文件
        FileInputStream inputStream = new FileInputStream("e:\\logo.png");

        //调用oss客户端，执行文件上传
        ossClient.putObject("java0817-001","pic/logo.png",inputStream);

        //关闭客户端
        ossClient.shutdown();

    }
}
