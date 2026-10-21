package com.firmament.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    /**
     * 根据指定分类ID查询关联的套餐数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 根据菜品id集合查询关联的套餐
     *
     * @param dishIds
     * @return
     */
    List<Long> selectSetmealIdsByDishIds(List<Long> dishIds);

}
