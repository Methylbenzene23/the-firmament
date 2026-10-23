package com.firmament.controller.user;

import com.firmament.constant.StatusConstant;
import com.firmament.result.Result;
import com.firmament.service.ShopService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("userShopController")
@RequestMapping("/user/shop")
@Api(tags = "店铺接口")
@Slf4j
public class ShopController {

    @Autowired
    private ShopService shopService;

    /**
     * 设置店铺营业状态
     *
     * @param status
     * @return
     */
    @PutMapping("/{status}")
    @ApiOperation("设置店铺营业状态")
    public Result switchStatus(@PathVariable Integer status){
        log.info("设置营业状态为: {}",status == StatusConstant.ENABLE ? "营业中":"打烊中");
        shopService.switchStatus(status);
        return Result.success();
    }

    /**
     * 获取店铺营业状态
     *
     * @return
     */
    @GetMapping("/status")
    @ApiOperation("获取店铺营业状态")
    private Result<Integer> getStatus(){
        log.info("获取店铺营业状态");
        Integer status = shopService.getStatus();
        return Result.success(status);
    }

}
