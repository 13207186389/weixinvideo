package com.pengyou.vidio.service.impl;


import com.pengyou.vidio.config.WeChatConfig;
import com.pengyou.vidio.domain.User;
import com.pengyou.vidio.mapper.UserMapper;
import com.pengyou.vidio.service.UserService;
import com.pengyou.vidio.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {



    @Autowired
    private WeChatConfig weChatConfig;

    @Autowired(required = false)
    private UserMapper userMapper;



    @Override
    public User saveWeChatUser(String code) {

        String accessTokenUrl = String.format(WeChatConfig.getOpenAccessTokenUrl(),weChatConfig.getOpenAppid(),weChatConfig.getOpenAppsecret(),code);

        //获取access_token
        Map<String ,Object> baseMap =  HttpUtils.doGet(accessTokenUrl);

        if(baseMap == null || baseMap.isEmpty()){ return  null; }
        String accessToken = (String)baseMap.get("access_token");
        String openId  = (String) baseMap.get("openid");

        //获取用户基本信息
        String userInfoUrl = String.format(WeChatConfig.getOpenUserInfoUrl(),accessToken,openId);
            //获取access_token
        Map<String ,Object> baseUserMap =  HttpUtils.doGet(userInfoUrl);

        if(baseUserMap == null || baseUserMap.isEmpty()){ return  null; }
        String nickname = (String)baseUserMap.get("nickname");
        Integer sex  = (Integer) baseUserMap.get("sex");
        String province = (String)baseUserMap.get("province");
        String city = (String)baseUserMap.get("city");
        String country = (String)baseUserMap.get("country");
        String headimgurl = (String)baseUserMap.get("headimgurl");
        StringBuilder sb = new StringBuilder(country).append("||").append(province).append("||").append(city);
        String finalAddress = sb.toString();
        try {
            //解决乱码
            nickname = new String(nickname.getBytes("ISO-8859-1"), "UTF-8");
            finalAddress = new String(finalAddress.getBytes("ISO-8859-1"), "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        User user = new User();
        user.setName(nickname);
        user.setHeadImg(headimgurl);
        user.setCity(finalAddress);
        user.setOpenid(openId);
        user.setSex(sex);
        user.setCreateTime(new Date());
        userMapper.save(user);
        return user;

    }
}
