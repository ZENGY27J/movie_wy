package com.stylefeng.guns.rest.common.persistence.utils;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.CreateBucketRequest;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class AliyunOSSUtil {
    private static String endpoint = "oss-cn-shenzhen.aliyuncs.com";
    private static String accessKeyId = "LTAI4Fk4BTCaWH5EcMtpEn8z";
    private static String accessKeySecret = "StDGf8qGZTSUyzG0JUzEANRYlRpWfn";
    private static String bucketName = "wymovie";
    //private String filedir = "";
    //private OSSClient ossClient;

    public static String upLoad(File file) {
        // 判断文件
        if (file == null) {
            return null;
        }
        OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        // 判断容器是否存在,不存在就创建
        if (!client.doesBucketExist(bucketName)) {
            client.createBucket(bucketName);
            CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
            createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
            client.createBucket(createBucketRequest);
        }
        // 设置文件路径和名称
        String fileUrl = file.getName();
        // 上传文件
        File uploadFile = new File("D:\\tmp", file.getName());
        FileInputStream input =null;
        try {
            input = new FileInputStream(uploadFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        PutObjectResult result = client.putObject(new PutObjectRequest(bucketName, fileUrl, file));
        // 设置权限(公开读)
        //client.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
        if (result != null) {
            System.out.println("------OSS文件上传成功------" + fileUrl);
        }
        return null;
    }
}
