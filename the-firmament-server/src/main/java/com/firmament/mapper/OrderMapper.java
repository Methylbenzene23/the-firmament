package com.firmament.mapper;

import com.firmament.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper {

    /**
     * 插入订单数据
     *
     * @param orders
     */
    void insert(Orders orders);
}
