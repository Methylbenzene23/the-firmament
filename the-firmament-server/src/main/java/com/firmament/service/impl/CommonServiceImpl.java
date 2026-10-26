package com.firmament.service.impl;

import com.firmament.service.CommonService;
import com.firmament.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

@Service
@Slf4j
public class CommonServiceImpl implements CommonService {

    @Autowired
    private AliOssUtil aliOssUtil;

    /**
     * 通过阿里云OSS上传文件到云端
     *
     * @param file
     * @return
     */
    @Override
    public String upload(MultipartFile file) {
        try {
            String originalFileName = file.getOriginalFilename();
            String subName = originalFileName.substring(originalFileName.lastIndexOf('.'));
            String uploadFileName = UUID.randomUUID() + subName;
            return aliOssUtil.upload(file.getBytes(),uploadFileName);
        } catch (IOException e) {
            log.info("文件上传失败:\n{}", Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 下载图片，返回二进制文件流
     *
     * @param url
     * @return
     */
    @Override
    public byte[] download(String url) {
        return aliOssUtil.download(url);
    }
}
