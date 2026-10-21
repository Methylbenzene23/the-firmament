package com.firmament.service.impl;

import com.firmament.dto.DishDTO;
import com.firmament.entity.Dish;
import com.firmament.entity.DishFlavor;
import com.firmament.mapper.DishMapper;
import com.firmament.mapper.DishFlavorMapper;
import com.firmament.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;


    /**
     * 新增菜品与口味数据
     *
     * @param dishDTO
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {

        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);

        dishMapper.insert(dish);
        Long dishId = dish.getId();

        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors != null && !flavors.isEmpty()){
            flavors.forEach(flavor -> {
                flavor.setDishId(dishId);
            });
        }
        dishFlavorMapper.insertBatch(flavors);
    }
}
