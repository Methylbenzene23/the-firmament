package com.firmament.controller.admin;

import com.firmament.constant.StatusConstant;
import com.firmament.dto.SetmealDTO;
import com.firmament.dto.SetmealPageQueryDTO;
import com.firmament.result.PageResult;
import com.firmament.result.Result;
import com.firmament.service.SetmealService;
import com.firmament.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("adminSetmealController")
@RequestMapping("/admin/setmeal")
@Api(tags = "套餐接口")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    /**
     * 分页查询菜套餐
     *
     * @param setmealPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("分页查询套餐")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO){
        log.info("套餐分页查询: {}",setmealPageQueryDTO);
        PageResult pageResult = setmealService.page(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 新增套餐
     *
     * @param setmealDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增套餐")
    public Result save(@RequestBody SetmealDTO setmealDTO){
        log.info("新增套餐: {}",setmealDTO);
        setmealService.saveWithDishes(setmealDTO);
        return Result.success();
    }

    /**
     * 切换套餐状态
     *
     * @param id
     * @param status
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("切换套餐状态(启售/停售)")
    public Result switchStatus(Long id, @PathVariable Integer status){
        log.info("{}套餐id: {}",status.equals(StatusConstant.ENABLE) ? "启售":"停售", id);
        setmealService.swtichStatus(id,status);
        return Result.success();
    }

    /**
     * 根据ID获取套餐
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据ID获取套餐")
    public Result<SetmealVO> getById(@PathVariable Long id){
        log.info("查询ID :{} 的套餐",id);
        SetmealVO setmealVO = setmealService.getById(id);
        return Result.success(setmealVO);
    }

    /**
     * 更新套餐信息和关联菜品
     *
     * @param setmealDTO
     * @return
     */
    @PutMapping
    @ApiOperation("更新套餐信息和关联菜品")
    public Result editWithSetmealDishes(@RequestBody SetmealDTO setmealDTO){
        log.info("更新套餐; {}",setmealDTO);
        setmealService.editWithSetmealDishes(setmealDTO);
        return Result.success();
    }

    /**
     * 批量删除菜品
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("批量删除菜品")
    public Result deleteWithSetmealDishes(@RequestParam List<Long> ids){
        log.info("批量删除菜品: {}",ids);
        setmealService.deleteWithSetmealDishIds(ids);
        return Result.success();
    }
}
