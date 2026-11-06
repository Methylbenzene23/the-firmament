package com.firmament.service;

import com.firmament.entity.AddressBook;

import java.util.List;

public interface AddressBookService {

    /**
     * 新增地址
     *
     * @param address
     * @return
     */
    void save(AddressBook address);

    /**
     * 查询当前用户所有地址
     *
     * @return
     */
    List<AddressBook> list();

    /**
     * 获得当前用户的默认地址
     *
     * @return
     */
    AddressBook getDefault();

    /**
     * 设为默认地址
     *
     * @param addressBook
     * @return
     */
    void setDefault(AddressBook addressBook);

    /**
     * 根据ID获取地址
     *
     * @param id
     * @return
     */
    AddressBook getById(Long id);

    /**
     * 修改地址信息
     *
     * @param address
     * @return
     */
    void edit(AddressBook address);

    /**
     * 删除地址
     *
     * @param id
     * @return
     */
    void delete(Long id);
}
