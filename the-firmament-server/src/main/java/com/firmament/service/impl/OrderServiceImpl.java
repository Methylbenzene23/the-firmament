package com.firmament.service.impl;

import com.firmament.constant.MessageConstant;
import com.firmament.context.BaseContext;
import com.firmament.dto.OrdersSubmitDTO;
import com.firmament.entity.AddressBook;
import com.firmament.entity.OrderDetail;
import com.firmament.entity.Orders;
import com.firmament.entity.ShoppingCart;
import com.firmament.exception.AddressBookBusinessException;
import com.firmament.exception.ShoppingCartBusinessException;
import com.firmament.mapper.AddressBookMapper;
import com.firmament.mapper.OrderDetailMapper;
import com.firmament.mapper.OrderMapper;
import com.firmament.mapper.ShoppingCartMapper;
import com.firmament.service.OrderService;
import com.firmament.vo.OrderSubmitVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private AddressBookMapper addressBookMapper;

    /**
     * 订单提交
     *
     * @param ordersSubmitDTO
     * @return
     */
    @Override
    @Transactional
    public OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO) {
        AddressBook addressBook = addressBookMapper.selectById(ordersSubmitDTO.getAddressBookId());
        if(addressBook == null){
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }

        Long userId = BaseContext.getCurrentId();
        ShoppingCart selectCarts = new ShoppingCart();
        selectCarts.setUserId(userId);
        List<ShoppingCart> cartList = shoppingCartMapper.select(selectCarts);
        if(cartList == null || cartList.isEmpty()){
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO,orders);
        orders.setPackAmount(orders.getPackAmount()/100);
        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setUserId(userId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setPayStatus(Orders.UN_PAID);
        orders.setPhone(addressBook.getPhone());
        orders.setAddress(addressBook.getProvinceName()
                +addressBook.getCityName()
                +addressBook.getDistrictName()
                +addressBook.getDetail());
        orders.setUserName(addressBook.getConsignee());
        orders.setConsignee(addressBook.getConsignee());
        orderMapper.insert(orders);

        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (ShoppingCart cart : cartList) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(cart,orderDetail);
            orderDetail.setOrderId(orders.getId());
            orderDetailList.add(orderDetail);
        }
        orderDetailMapper.insertBatch(orderDetailList);

        shoppingCartMapper.deleteByUserId(userId);

        return OrderSubmitVO.builder()
                .id(orders.getId())
                .orderNumber(orders.getNumber())
                .orderAmount(orders.getAmount())
                .orderTime(orders.getOrderTime())
                .build();
    }
}
