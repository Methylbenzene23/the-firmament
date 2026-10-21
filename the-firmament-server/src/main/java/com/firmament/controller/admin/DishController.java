package com.firmament.controller.admin;

import com.firmament.dto.DishDTO;
import com.firmament.dto.DishPageQueryDTO;
import com.firmament.result.PageResult;
import com.firmament.result.Result;
import com.firmament.service.DishService;
import com.firmament.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    /**
     * 新增菜品
     *
     * @param dishDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增菜品")
    public Result save(@RequestBody DishDTO dishDTO){
        log.info("新增菜品: {}",dishDTO);
        dishService.saveWithFlavor(dishDTO);
        return Result.success();
    }

    /**
     * 菜品分页查询
     *
     * @param dishPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        log.info("菜品分页查询: {}",dishPageQueryDTO);
        PageResult pageResult = dishService.page(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 批量删除菜品
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("批量删除菜品")
    public Result deleteBatch(@RequestParam List<Long> ids){
        log.info("批量删除菜品: {}",ids);
        dishService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 根据ID查询菜品和口味
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据ID查询菜品")
    public Result<DishVO> getByIdWithFlavor(@PathVariable Long id){
        log.info("根据ID查询菜品: {}",id);
        DishVO dishVO = dishService.getByIdWithFlavor(id);
        return Result.success(dishVO);
    }

    /**
     * 修改菜品信息或口味
     *
     * @param dishDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改菜品信息")
    public Result edit(@RequestBody DishDTO dishDTO){
        log.info("修改菜品信息: {}",dishDTO);
        dishService.editWithFlavor(dishDTO);
        return Result.success();
    }

}
