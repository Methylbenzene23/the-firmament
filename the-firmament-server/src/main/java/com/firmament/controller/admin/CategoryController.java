package com.firmament.controller.admin;

import com.firmament.constant.CategoryTypeConstant;
import com.firmament.constant.MessageConstant;
import com.firmament.constant.StatusConstant;
import com.firmament.dto.CategoryDTO;
import com.firmament.dto.CategoryPageQueryDTO;
import com.firmament.entity.Category;
import com.firmament.result.PageResult;
import com.firmament.result.Result;
import com.firmament.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/category")
@Api(tags = "菜品分类相关接口")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增菜品分类
     *
     * @param categoryDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增菜品分类")
    public Result save(@RequestBody CategoryDTO categoryDTO){
        log.info("新增菜品分类: {}",categoryDTO);
        categoryService.save(categoryDTO);
        return Result.success();
    }

    /**
     * 根据分类名和类型分页查询菜品分类
     *
     * @param categoryPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("分页查询菜品分类")
    public Result<PageResult> page(CategoryPageQueryDTO categoryPageQueryDTO){
        log.info("分页查询菜品分类: {}", categoryPageQueryDTO);
        PageResult pageResult = categoryService.page(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 切换菜品分类状态
     *
     * @param id
     * @param status
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("切换菜品分类禁用/启用")
    public Result switchStatus(Long id, @PathVariable Integer status){
        log.info("{}菜品分类id: {}",status.equals(StatusConstant.ENABLE) ? "启用":"禁用", id);
        categoryService.switchStatus(id,status);
        return Result.success();
    }

    /**
     * 修改分类
     *
     * @param categoryDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改菜品分类信息")
    public Result edit(@RequestBody CategoryDTO categoryDTO){
        log.info("修改菜品分类信息 {}",categoryDTO);
        categoryService.edit(categoryDTO);
        return Result.success();
    }

    /**
     * 根据ID删除菜品分类
     *
     * @param id
     * @return
     */
    @DeleteMapping
    @ApiOperation("根据ID删除菜品分类")
    public Result deleteById(Long id){
        log.info("删除ID: {} 的菜品分类",id);
        categoryService.deleteById(id);
        return Result.success();
    }

    /**
     * 根据类型查询菜品分类
     *
     * @param type
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("查询指定分类的菜品分类")
    public Result<Category[]> list(Integer type){
        log.info("查询“{}”类型的菜品分类",type.equals(CategoryTypeConstant.DISHES_CLASSIFY) ? "菜品分类":"套餐分类");
        Category[] categories = categoryService.list(type);
        return Result.success(categories);
    }

}
