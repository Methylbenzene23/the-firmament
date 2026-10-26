package com.firmament.service;

import com.firmament.dto.SetmealDTO;
import com.firmament.dto.SetmealPageQueryDTO;
import com.firmament.entity.Setmeal;
import com.firmament.result.PageResult;
import com.firmament.vo.DishItemVO;
import com.firmament.vo.SetmealVO;

import java.util.List;

public interface SetmealService {

    /**
     * 分页查询菜套餐
     *
     * @param setmealPageQueryDTO
     * @return
     */
    PageResult page(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 新增套餐并保存套餐内包含的菜品
     *
     * @param setmealDTO
     */
    void saveWithDishes(SetmealDTO setmealDTO);

    /**
     * 切换套餐状态
     *
     * @param id
     * @param status
     * @return
     */
    void swtichStatus(Long id, Integer status);

    /**
     * 根据ID获取套餐
     *
     * @param id
     * @return
     */
    SetmealVO getById(Long id);

    /**
     * 更新套餐信息和关联菜品
     *
     * @param setmealDTO
     * @return
     */
    void editWithSetmealDishes(SetmealDTO setmealDTO);

    /**
     * 批量删除菜品
     *
     * @param setmealIds
     * @return
     */
    void deleteWithSetmealDishIds(List<Long> setmealIds);

    /**
     * 根据分类ID查询启售套餐
     *
     * @return
     */
    List<Setmeal> list(Long categoryId);

    /**
     * 查询套餐中包含的菜品简要信息
     *
     * @param id
     * @return
     */
    List<DishItemVO> getBySetmealId(Long id);
}
