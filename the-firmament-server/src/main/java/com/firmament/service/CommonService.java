package com.firmament.service;

import org.springframework.web.multipart.MultipartFile;

public interface CommonService {

    String upload(MultipartFile file);

    /**
     * 下载图片，返回二进制文件流
     *
     * @param url
     * @return
     */
    byte[] download(String url);
}
