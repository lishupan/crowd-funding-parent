package com.offcn.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.Data;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Data
public class OssTemplate {

    private String endpoint;//服务器访问端点地址
    private String bucketDomain;//桶域名 java0817-001.oss-cn-guangzhou.aliyuncs.com
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;//存储桶的名称

    //上传文件到oss上传方法
    public String upload(InputStream inputStream,String fileName){

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String folderName  = dateFormat.format(new Date());//按照天创建目录名称

        //生成一个带随机数文件名，避免重复名称
      fileName=  UUID.randomUUID().toString().replaceAll("-","")+"_"+fileName;

      //创建ossClient
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        //调用ossclient执行文件上传
        ossClient.putObject(bucketName,"pic/"+folderName+"/"+fileName,inputStream);

        //关闭客户端
        ossClient.shutdown();

        //拼接文件访问时间路径
        String url="https://"+bucketDomain+"/pic/"+folderName+"/"+fileName;

        System.out.println("上传文件路径:"+url);

        return url;


    }


}
