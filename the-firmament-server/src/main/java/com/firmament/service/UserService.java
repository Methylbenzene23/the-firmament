package com.firmament.service;

import com.firmament.dto.UserLoginDTO;
import com.firmament.entity.User;
import com.firmament.vo.UserLoginVO;

public interface UserService {

    /**
     * 微信用户登录
     *
     * @param userLoginDTO
     * @return
     */
    User login(UserLoginDTO userLoginDTO);
}
