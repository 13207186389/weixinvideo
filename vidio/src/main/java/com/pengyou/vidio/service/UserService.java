package com.pengyou.vidio.service;


import com.pengyou.vidio.domain.User;

/**
 *用户业务接口类
 */
public interface UserService {


     User saveWeChatUser(String code);

}
