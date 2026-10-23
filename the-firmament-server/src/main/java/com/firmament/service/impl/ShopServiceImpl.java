package com.firmament.service.impl;

import com.firmament.constant.RedisKeyConstant;
import com.firmament.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    /**
     * 设置店铺营业状态
     *
     * @param status
     * @return
     */
    @Override
    public void switchStatus(Integer status) {
        redisTemplate.opsForValue().set(RedisKeyConstant.SHOP_STATUS,status);
    }

    @Override
    public Integer getStatus() {
        return (Integer) redisTemplate.opsForValue().get(RedisKeyConstant.SHOP_STATUS);
    }
}
