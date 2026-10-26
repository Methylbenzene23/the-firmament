package com.firmament.service.impl;

import com.firmament.constant.CategoryTypeConstant;
import com.firmament.constant.MessageConstant;
import com.firmament.constant.StatusConstant;
import com.firmament.dto.CategoryDTO;
import com.firmament.dto.CategoryPageQueryDTO;
import com.firmament.entity.Category;
import com.firmament.exception.DeletionNotAllowedException;
import com.firmament.mapper.CategoryMapper;
import com.firmament.mapper.DishMapper;
import com.firmament.mapper.SetmealDishMapper;
import com.firmament.result.PageResult;
import com.firmament.service.CategoryService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealDishMapper setMealDishMapper;

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
        if(categoryPageQueryDTO.getPage() == 0 && categoryPageQueryDTO.getPageSize() == 0){
            categoryPageQueryDTO.setPage(1);
            categoryPageQueryDTO.setPageSize(1000);
        }
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
        if(dishMapper.countByCategoryId(id) > 0){
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }
        if(setMealDishMapper.countByCategoryId(id) > 0){
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }
        categoryMapper.delete(id);
    }

    /**
     * 根据类型查询菜品分类
     *
     * @param type
     * @return
     */
    @Override
    public List<Category> list(Integer type) {
        if(type == null){
            List<Category> categories = categoryMapper.selectByType(CategoryTypeConstant.DISHES_CLASSIFY);
            categories.addAll(categoryMapper.selectByType(CategoryTypeConstant.DISHES_SET));
            return categories;
        }
        return categoryMapper.selectByType(type);
    }

}
