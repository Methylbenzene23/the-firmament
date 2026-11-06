package com.firmament.service.impl;

import com.firmament.context.BaseContext;
import com.firmament.dto.ShoppingCartDTO;
import com.firmament.entity.Dish;
import com.firmament.entity.Setmeal;
import com.firmament.entity.ShoppingCart;
import com.firmament.mapper.DishMapper;
import com.firmament.mapper.SetmealMapper;
import com.firmament.mapper.ShoppingCartMapper;
import com.firmament.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 查看购物车
     *
     * @return
     */
    @Override
    public List<ShoppingCart> list() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(BaseContext.getCurrentId());
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.select(shoppingCart);
        return shoppingCarts;
    }

    /**
     * 购物车内添加菜品
     *
     * @param shoppingCartDTO
     * @return
     */
    @Override
    @Transactional
    public void add(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.select(shoppingCart);

        if(shoppingCartList != null && !shoppingCartList.isEmpty()){
            ShoppingCart cart = shoppingCartList.get(0);
            cart.setNumber(cart.getNumber() + 1);
            shoppingCartMapper.updateNumberById(cart);
        }else{
            Long dishId = shoppingCartDTO.getDishId();
            Long setmealId = shoppingCartDTO.getSetmealId();
            if(dishId != null){
                Dish dish = dishMapper.selectById(dishId);
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
            }else if(setmealId != null){
                Setmeal setmeal =setmealMapper.selectById(setmealId);
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingCart);
        }
    }

    /**
     * 购物车内添加菜品
     *
     * @param shoppingCartDTO
     * @return
     */
    @Override
    public void sub(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());
        ShoppingCart selectedCart = shoppingCartMapper.select(shoppingCart).get(0);
        if(selectedCart.getNumber() == 1){
            shoppingCartMapper.delete(selectedCart);
        }else{
            selectedCart.setNumber(selectedCart.getNumber() - 1);
            shoppingCartMapper.updateNumberById(selectedCart);
        }
    }

    /**
     * 清空购物车
     *
     * @return
     */
    @Override
    public void deleteAll() {
        shoppingCartMapper.deleteByUserId(BaseContext.getCurrentId());
    }
}
