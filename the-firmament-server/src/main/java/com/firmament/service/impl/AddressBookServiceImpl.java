package com.firmament.service.impl;

import com.firmament.constant.StatusConstant;
import com.firmament.context.BaseContext;
import com.firmament.entity.AddressBook;
import com.firmament.mapper.AddressBookMapper;
import com.firmament.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressBookServiceImpl implements AddressBookService {

    @Autowired
    private AddressBookMapper addressBookMapper;

    /**
     * 新增地址
     *
     * @param address
     * @return
     */
    @Override
    public void save(AddressBook address) {
        address.setUserId(BaseContext.getCurrentId());
        address.setIsDefault(StatusConstant.DISABLE);
        addressBookMapper.insert(address);
    }

    /**
     * 查询当前用户所有地址
     *
     * @return
     */
    @Override
    public List<AddressBook> list() {
        return addressBookMapper.selectByUserId(BaseContext.getCurrentId());
    }

    /**
     * 获得当前用户的默认地址
     *
     * @return
     */
    @Override
    public AddressBook getDefault() {
        return addressBookMapper.selectDefaultByUserId(BaseContext.getCurrentId());
    }

    /**
     * 设为默认地址
     *
     * @param address
     * @return
     */
    @Override
    @Transactional
    public void setDefault(AddressBook address) {
        Long userId = BaseContext.getCurrentId();
        AddressBook addressBook = new AddressBook();
        addressBook.setUserId(userId);
        addressBook.setIsDefault(StatusConstant.DISABLE);
        addressBookMapper.update(addressBook);

        address.setUserId(userId);
        address.setIsDefault(StatusConstant.ENABLE);
        addressBookMapper.update(address);
    }

    /**
     * 根据ID获取地址
     *
     * @param id
     * @return
     */
    @Override
    public AddressBook getById(Long id) {
        return addressBookMapper.selectById(id);
    }

    /**
     * 修改地址信息
     *
     * @param address
     * @return
     */
    @Override
    public void edit(AddressBook address) {
        addressBookMapper.update(address);
    }

    /**
     * 删除地址
     *
     * @param id
     * @return
     */
    @Override
    @Transactional
    public void delete(Long id) {
        AddressBook address = addressBookMapper.selectById(id);
        List<AddressBook> addressList = addressBookMapper.selectByUserId(address.getUserId());
        if(addressList.size() > 1){
            AddressBook newAddress = addressList.get(0);
            if(newAddress.getIsDefault() == StatusConstant.ENABLE){
                newAddress = addressList.get(1);
            }
            newAddress.setIsDefault(StatusConstant.ENABLE);
            addressBookMapper.update(newAddress);
        }
        addressBookMapper.deleteById(id);
    }
}
