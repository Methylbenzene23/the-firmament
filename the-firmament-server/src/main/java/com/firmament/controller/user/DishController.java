package com.firmament.controller.user;

import com.firmament.entity.Dish;
import com.firmament.result.Result;
import com.firmament.service.DishService;
import com.firmament.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userDishController")
@RequestMapping("/user/dish")
@Api(tags = "菜品接口")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    /**
     * 根据分类ID查询菜品与口味
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类ID查询菜品")
    public Result<List<DishVO>> getByCategoryIdWithFlavor(Long categoryId){
        log.info("查询分类ID: {} 下的所有菜品",categoryId);
        List<DishVO> dishes = dishService.getByCategoryIdWithFlavor(categoryId);
        return Result.success(dishes);
    }

}
