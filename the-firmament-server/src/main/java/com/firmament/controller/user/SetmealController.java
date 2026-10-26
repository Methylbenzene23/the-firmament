package com.firmament.controller.user;

import com.firmament.constant.StatusConstant;
import com.firmament.dto.SetmealPageQueryDTO;
import com.firmament.entity.Setmeal;
import com.firmament.result.PageResult;
import com.firmament.result.Result;
import com.firmament.service.SetmealService;
import com.firmament.vo.DishItemVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userSetmealController")
@RequestMapping("/user/setmeal")
@Api(tags = "套餐接口")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    /**
     * 根据分类ID查询启售套餐
     *
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类ID查询启售套餐")
    public Result<List<Setmeal>> list(Long categoryId){
        log.info("查询分类ID: {} 启售的套餐",categoryId);
        List<Setmeal> setmeals = setmealService.list(categoryId);
        return Result.success(setmeals);
    }

    /**
     * 查询套餐中包含的菜品简要信息
     *
     * @param id
     * @return
     */
    @GetMapping("/dish/{id}")
    @ApiOperation("查询套餐内包含的菜品简要信息")
    public Result<List<DishItemVO>> getBySetmealId(@PathVariable Long id){
        log.info("查询套餐ID: {} 下的菜品",id);
        List<DishItemVO> dishItemVOS = setmealService.getBySetmealId(id);
        return Result.success(dishItemVOS);
    }

}
