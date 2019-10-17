package com.wuyan.oss;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.PutObjectRequest;

import java.io.File;

public class Oss {
    public static void upload(File file,String key){
        String bucket = "wymovie";
        String endPoint = "http://oss-cn-shenzhen.aliyuncs.com";
        String accessKeyId = "LTAI4Fk4BTCaWH5EcMtpEn8z";
        String accessSecret = "StDGf8qGZTSUyzG0JUzEANRYlRpWfn";
        OSSClient ossClient = new OSSClient(endPoint, accessKeyId, accessSecret);
        ossClient.putObject(new PutObjectRequest(bucket,key, file));
    }
}
