package com.firmament.mapper;

import com.firmament.dto.CategoryPageQueryDTO;
import com.firmament.entity.Category;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CategoryMapper {

    /**
     * 新增菜品分类
     * @param category
     * @return
     */
    @Insert("insert into category(id, type, name, sort, status, create_time, update_time, create_user, update_user) " +
            "values (#{id}, #{type}, #{name}, #{sort}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    void insert(Category category);

    /**
     * 根据分类名和类型分页查询菜品分类
     * @param categoryPageQueryDTO
     * @return
     */
    Page<Category> pageSelect(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 更新菜品分类信息
     * @param category
     * @return
     */
    void update(Category category);

    /**
     * 删除菜品分类
     *
     * @param id
     * @return
     */
    @Delete("delete from category where id = #{id}")
    void delete(Long id);

    /**
     * 根据类型查询菜品分类
     *
     * @param type
     * @return
     */
    @Select("select * from category where type = #{type}")
    Category[] selectByType(Integer type);
}
