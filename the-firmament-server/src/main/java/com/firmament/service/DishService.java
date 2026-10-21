package com.firmament.service;

import com.firmament.dto.DishDTO;
import org.springframework.stereotype.Service;

public interface DishService {

    /**
     * 新增菜品与口味数据
     *
     * @param dishDTO
     */
    void saveWithFlavor(DishDTO dishDTO);
}
