package com.firmament.service;

import com.firmament.dto.CategoryDTO;
import com.firmament.dto.CategoryPageQueryDTO;
import com.firmament.entity.Category;
import com.firmament.result.PageResult;

import java.util.List;

public interface CategoryService {

    /**
     * 新增菜品分类
     *
     * @param categoryDTO
     * @return
     */
    void save(CategoryDTO categoryDTO);

    /**
     * 根据分类名和类型分页查询菜品分类
     *
     * @param categoryPageQueryDTO
     * @return
     */
    PageResult page(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 切换菜品分类状态
     *
     * @param id
     * @param status
     * @return
     */
    void switchStatus(Long id, Integer status);

    /**
     * 修改分类
     *
     * @param categoryDTO
     * @return
     */
    void edit(CategoryDTO categoryDTO);

    /**
     * 根据ID删除菜品分类
     *
     * @param id
     * @return
     */
    void deleteById(Long id);

    /**
     * 根据类型查询菜品分类
     *
     * @param type
     * @return
     */
    List<Category> list(Integer type);
}
