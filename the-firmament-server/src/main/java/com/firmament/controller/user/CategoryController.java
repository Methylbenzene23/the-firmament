package com.firmament.controller.user;

import com.firmament.entity.Category;
import com.firmament.result.Result;
import com.firmament.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userCategoryController")
@RequestMapping("/user/category")
@Api(tags = "分类接口")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 查询所有分类
     *
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("查询所有分类")
    public Result<List<Category>> list(Integer type){
        log.info("查询所有分类; {}",type);
        List<Category> categories = categoryService.list(type);
        return Result.success(categories);
    }

}
