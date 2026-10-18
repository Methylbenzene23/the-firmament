package com.firmament.service.impl;

import com.firmament.constant.StatusConstant;
import com.firmament.context.BaseContext;
import com.firmament.dto.CategoryDTO;
import com.firmament.dto.CategoryPageQueryDTO;
import com.firmament.entity.Category;
import com.firmament.mapper.CategoryMapper;
import com.firmament.result.PageResult;
import com.firmament.service.CategoryService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 新增菜品分类
     *
     * @param categoryDTO
     * @return
     */
    @Override
    public void save(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO,category);
        category.setStatus(StatusConstant.DISABLE);
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        category.setCreateUser(BaseContext.getCurrentId());
        category.setUpdateUser(BaseContext.getCurrentId());

        categoryMapper.insert(category);
    }

    /**
     * 根据分类名和类型分页查询菜品分类
     *
     * @param categoryPageQueryDTO
     * @return
     */
    @Override
    public PageResult page(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageHelper.startPage(categoryPageQueryDTO.getPage(),categoryPageQueryDTO.getPageSize());
        Page<Category> page = categoryMapper.pageSelect(categoryPageQueryDTO);
        PageResult pageResult = new PageResult(page.getTotal(),page.getResult());
        return pageResult;
    }

    /**
     * 切换菜品分类状态
     *
     * @param id
     * @param status
     * @return
     */
    @Override
    public void switchStatus(Long id, Integer status) {
        Category category = Category.builder()
                .id(id)
                .status(status)
                .updateTime(LocalDateTime.now())
                .updateUser(BaseContext.getCurrentId())
                .build();
        categoryMapper.update(category);
    }

    /**
     * 修改分类
     *
     * @param categoryDTO
     * @return
     */
    @Override
    public void edit(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO,category);
        category.setUpdateTime(LocalDateTime.now());
        category.setUpdateUser(BaseContext.getCurrentId());

        categoryMapper.update(category);
    }

    /**
     * 根据ID删除菜品分类
     *
     * @param id
     * @return
     */
    @Override
    public void deleteById(Long id) {
        categoryMapper.delete(id);
    }

    /**
     * 根据类型查询菜品分类
     *
     * @param type
     * @return
     */
    @Override
    public Category[] list(Integer type) {
        Category[] categories = categoryMapper.selectByType(type);
        return categories;
    }

}
