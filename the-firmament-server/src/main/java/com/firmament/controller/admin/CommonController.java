package com.firmament.controller.admin;

import com.firmament.constant.MessageConstant;
import com.firmament.result.Result;
import com.firmament.service.CommonService;
import com.firmament.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

@RestController("adminCommonController")
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
@Slf4j
public class CommonController {

    @Autowired
    private CommonService commonService;

    /**
     * 通过阿里云OSS上传文件到云端
     *
     * @param file
     * @return
     */
    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload(MultipartFile file) {
        log.info("文件上传: {}",file);
        String url = commonService.upload(file);
        if(url != null){
            return Result.success(url);
        }
        return Result.error(MessageConstant.UPLOAD_FAILED);
    }

}
