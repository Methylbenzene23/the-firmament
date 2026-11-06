package com.firmament.service;

import com.firmament.dto.ShoppingCartDTO;
import com.firmament.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {

    /**
     * 查看购物车
     *
     * @return
     */
    List<ShoppingCart> list();

    /**
     * 购物车内添加菜品
     *
     * @param shoppingCartDTO
     * @return
     */
    void add(ShoppingCartDTO shoppingCartDTO);

    /**
     * 减少一项购物车商品
     *
     * @param shoppingCartDTO
     * @return
     */
    void sub(ShoppingCartDTO shoppingCartDTO);

    /**
     * 清空购物车
     *
     * @return
     */
    void deleteAll();
}
