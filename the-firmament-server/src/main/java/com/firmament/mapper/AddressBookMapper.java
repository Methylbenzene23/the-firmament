package com.firmament.mapper;

import com.firmament.entity.AddressBook;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AddressBookMapper {

    /**
     * 新增地址
     *
     * @param address
     * @return
     */
    void insert(AddressBook address);

    /**
     * 查询当前用户所有地址
     *
     * @return
     */
    @Select("select * from address_book where user_id = #{userId}")
    List<AddressBook> selectByUserId(Long userId);

    /**
     * 获得当前用户的默认地址
     *
     * @return
     */
    @Select("select * from address_book where user_id = #{userId} and is_default = 1")
    AddressBook selectDefaultByUserId(Long userId);

    /**
     * 更新地址信息
     *
     * @param address
     */
    void update(AddressBook address);

    /**
     * 根据id查询地址
     *
     * @param id
     * @return
     */
    @Select("select * from address_book where id = #{id}")
    AddressBook selectById(Long id);

    /**
     * 删除地址
     *
     * @param id
     * @return
     */
    @Delete("delete from address_book where id = #{id}")
    void deleteById(Long id);
}
