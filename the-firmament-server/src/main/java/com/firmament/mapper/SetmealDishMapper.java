package com.firmament.mapper;

import com.firmament.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
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

    /**
     * 批量插入菜品套餐信息
     *
     * @param setmealDishes
     */
    void insertBatch(List<SetmealDish> setmealDishes);

    /**
     * 根据套餐ID查询关联的菜品
     *
     * @param setmealId
     * @return
     */
    @Select("select * from setmeal_dish where setmeal_id = #{setmealId}")
    List<SetmealDish> selectSetmealDishesBySetmealId(Long setmealId);

    /**
     * 根据套餐ID删除与菜品的关联信息
     *
     * @param setmealId
     */
    @Delete("delete from setmeal_dish where setmeal_id = #{setmealId}")
    void deleteBySetmealId(Long setmealId);

    /**
     * 根据套餐ID批量删除与菜品的关联信息
     *
     * @param setmealIds
     */
    void deleteBatchBySetmealId(List<Long> setmealIds);
}
