package com.firmament.controller.user;

import com.firmament.service.CommonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("userCommonController")
@RequestMapping("/user/common")
@Api(tags = "通用接口")
@Slf4j
public class CommonController {

    @Autowired
    private CommonService commonService;

    /**
     * 下载图片，返回二进制文件流
     *
     * @param name
     * @return
     */
    @GetMapping("/download")
    @ApiOperation("下载图片")
    public ResponseEntity<byte[]> download(String name){
        log.info("下载文件: {}",name);

        byte[] content = commonService.download(name);

        // 从文件名推断图片类型，推断不出来就按二进制流返回
        String fileName = name.substring(name.lastIndexOf('/') + 1);
        MediaType mediaType = MediaTypeFactory.getMediaType(fileName)
                .orElse(MediaType.APPLICATION_OCTET_STREAM);

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                .body(content);
    }

}
