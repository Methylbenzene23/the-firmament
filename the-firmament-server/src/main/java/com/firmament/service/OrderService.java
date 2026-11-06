package com.firmament.service;

import com.firmament.dto.OrdersSubmitDTO;
import com.firmament.vo.OrderSubmitVO;

public interface OrderService {

    /**
     * 订单提交
     *
     * @param ordersSubmitDTO
     * @return
     */
    OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO);
}
