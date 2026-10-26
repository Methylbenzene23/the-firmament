package com.firmament.mapper;

import com.firmament.annotation.MapperAutoFill;
import com.firmament.dto.SetmealDTO;
import com.firmament.dto.SetmealPageQueryDTO;
import com.firmament.entity.Setmeal;
import com.firmament.enumeration.OperationType;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealMapper {

    /**
     * 分页查询菜套餐
     *
     * @param setmealPageQueryDTO
     * @return
     */
    Page<Setmeal> pageSelect(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 新增套餐
     *
     * @param setmeal
     */
    @MapperAutoFill(OperationType.INSERT)
    void insert(Setmeal setmeal);

    /**
     * 更新套餐信息
     *
     * @param setmeal
     */
    @MapperAutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);

    /**
     * 根据ID查询套餐
     *
     * @param id
     * @return
     */
    @Select("select * from setmeal where id = #{id}")
    Setmeal selectById(Long id);

    /**
     * 根据ID批量删除套餐
     *
     * @param setmealIds
     */
    void deleteBatch(List<Long> setmealIds);

    /**
     * 根据分类ID查询启售套餐
     *
     * @return
     */
    @Select("select * from setmeal where category_id = #{categoryId} and status = 1")
    List<Setmeal> selectByCategoryId(Long CategoryId);
}
