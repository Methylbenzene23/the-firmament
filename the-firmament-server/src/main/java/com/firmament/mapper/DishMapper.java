package com.firmament.mapper;

import com.firmament.annotation.MapperAutoFill;
import com.firmament.dto.DishPageQueryDTO;
import com.firmament.entity.Dish;
import com.firmament.enumeration.OperationType;
import com.firmament.vo.DishVO;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 获得指定分类ID下菜品的数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 插入菜品
     *
     * @param dish
     */
    @MapperAutoFill(OperationType.INSERT)
    void insert(Dish dish);

    /**
     * 菜品分页查询
     *
     * @param dishPageQueryDTO
     * @return
     */
    Page<DishVO> pageSelect(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 根据id查询菜品
     *
     * @return
     */
    @Select("select * from dish where id = #{id}")
    Dish selectById(Long id);

    /**
     * 根据批量ID删除菜品
     *
     * @param ids
     */
    void deleteByIds(List<Long> ids);
}
