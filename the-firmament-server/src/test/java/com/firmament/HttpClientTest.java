package com.firmament;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.bouncycastle.util.encoders.UTF8;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

@SpringBootTest
public class HttpClientTest {

    @Test
    public void testHttpGet() throws IOException {
        //使用HttpClients创建HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //创建Http请求对象，并指定请求uri
        HttpGet httpGet = new HttpGet("http://localhost:8080/user/shop/status");
        //使用HttpClient执行Http请求，并获取响应结果
        CloseableHttpResponse response = httpClient.execute(httpGet);
        //从响应结果对象中获取状态码
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("Status Code: " + statusCode);
        //从相应结果中获取响应体
        HttpEntity entity = response.getEntity();
        String body = EntityUtils.toString(entity);
        System.out.println("Body: " + body);
        //关闭资源
        response.close();
        httpClient.close();
    }

    @Test
    public void testHttpPost() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://localhost:8080/admin/employee/login");

        //构造请求体内容: 构造json字符串并传入
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username","admin");
        jsonObject.put("password","123456");
        StringEntity httpEntity = new StringEntity(jsonObject.toJSONString());
        //设置编码格式和内容格式
        httpEntity.setContentEncoding("utf-8");
        httpEntity.setContentType("application/json");

        httpPost.setEntity(httpEntity);
        CloseableHttpResponse response = httpClient.execute(httpPost);
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("Status Code: " + statusCode);

        HttpEntity entity = response.getEntity();
        String body = EntityUtils.toString(entity);
        System.out.println("Body: "+body);

        response.close();
        httpClient.close();
    }

}
