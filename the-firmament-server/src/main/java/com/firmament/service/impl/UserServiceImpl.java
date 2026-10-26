package com.firmament.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.firmament.constant.MessageConstant;
import com.firmament.dto.UserLoginDTO;
import com.firmament.entity.User;
import com.firmament.exception.LoginFailedException;
import com.firmament.mapper.UserMapper;
import com.firmament.properties.WeChatProperties;
import com.firmament.service.UserService;
import com.firmament.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private static final String wxLoginUrl = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private WeChatProperties weChatProperties;

    @Autowired
    private UserMapper userMapper;

    /**
     * 微信用户登录
     *
     * @param userLoginDTO
     * @return
     */
    @Override
    public User login(UserLoginDTO userLoginDTO) {
        String openId = this.getOpenid(userLoginDTO.getCode());

        if(openId == null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        User user = userMapper.getByOpenId(openId);
        if(user == null){
            user = User.builder()
                    .openid(openId)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
        }

        return user;
    }

    /**
     * 根据code获取openid
     *
     * @param code
     * @return
     */
    private String getOpenid(String code){
        Map<String,String> paraMap = new HashMap<>();
        paraMap.put("appid",weChatProperties.getAppid());
        paraMap.put("secret",weChatProperties.getSecret());
        paraMap.put("js_code",code);
        paraMap.put("grant_type","authorization_code");

        String responseString = HttpClientUtil.doGet(wxLoginUrl,paraMap);
        JSONObject responseObject = JSON.parseObject(responseString);
        String openid = responseObject.getString("openid");
        return openid;
    }
}
