package com.firmament.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Data
@AllArgsConstructor
@Slf4j
public class AliOssUtil {

    /**
     * 读取对象流时使用的缓冲区大小
     */
    private static final int BUFFER_SIZE = 8192;

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;

    /**
     * 文件上传
     *
     * @param bytes
     * @param objectName
     * @return
     */
    public String upload(byte[] bytes, String objectName) {

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            // 创建PutObject请求。
            ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(bytes));
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }

        //文件访问路径规则 https://BucketName.Endpoint/ObjectName
        StringBuilder stringBuilder = new StringBuilder("https://");
        stringBuilder
                .append(bucketName)
                .append(".")
                .append(endpoint)
                .append("/")
                .append(objectName);

        log.info("文件上传到:{}", stringBuilder.toString());

        return stringBuilder.toString();
    }

    /**
     * 下载文件，返回二进制文件流
     *
     * @param url
     * @return 文件字节内容
     */
    public byte[] download(String url) {
        String objectName = url.trim();
        int schemeIndex = objectName.indexOf("://");
        int pathIndex = objectName.indexOf('/', schemeIndex + 3);
        objectName = pathIndex >= 0 ? objectName.substring(pathIndex + 1) : "";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try (InputStream in = ossClient.getObject(bucketName, objectName).getObjectContent();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[BUFFER_SIZE];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }

            byte[] content = out.toByteArray();
            log.info("从OSS下载文件成功: bucket={}, object={}, size={} B", bucketName, objectName, content.length);
            return content;

        } catch (OSSException oe) {
            log.error("OSS 下载失败, object={}, errorCode={}, requestId={}, message={}",
                    objectName, oe.getErrorCode(), oe.getRequestId(), oe.getErrorMessage());
            throw new RuntimeException("文件下载失败: " + oe.getErrorMessage(), oe);
        } catch (ClientException ce) {
            log.error("OSS 客户端异常, object={}, message={}", objectName, ce.getMessage());
            throw new RuntimeException("文件下载失败: " + ce.getMessage(), ce);
        } catch (IOException e) {
            log.error("读取OSS对象流失败, object={}", objectName, e);
            throw new RuntimeException("文件下载失败", e);
        } finally {
            ossClient.shutdown();
        }
    }

}
