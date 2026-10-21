package com.firmament.service;

import com.firmament.dto.DishDTO;
import com.firmament.dto.DishPageQueryDTO;
import com.firmament.result.PageResult;
import com.firmament.vo.DishVO;
import org.springframework.stereotype.Service;

import java.util.List;

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

    /**
     * 批量删除菜品
     *
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    /**
     * 根据ID查询菜品和口味
     *
     * @param id
     * @return
     */
    DishVO getByIdWithFlavor(Long id);

    /**
     * 修改菜品信息或口味
     *
     * @param dishDTO
     * @return
     */
    void editWithFlavor(DishDTO dishDTO);
}
