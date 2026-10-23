package com.firmament.service;

public interface ShopService {

    /**
     * 设置店铺营业状态
     *
     * @param status
     * @return
     */
    void switchStatus(Integer status);

    /**
     * 获取店铺营业状态
     *
     * @return
     */
    Integer getStatus();
}
