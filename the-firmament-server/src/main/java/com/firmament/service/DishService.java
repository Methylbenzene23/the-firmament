package com.firmament.service;

import com.firmament.dto.DishDTO;
import com.firmament.dto.DishPageQueryDTO;
import com.firmament.result.PageResult;
import org.springframework.stereotype.Service;

public interface DishService {

    /**
     * 新增菜品与口味数据
     *
     * @param dishDTO
     */
    void saveWithFlavor(DishDTO dishDTO);

    /**
     * 菜品分页查询
     *
     * @param dishPageQueryDTO
     * @return
     */
    PageResult page(DishPageQueryDTO dishPageQueryDTO);
}
