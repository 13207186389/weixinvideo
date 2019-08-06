package com.pengyou.vidio.controller;

import com.pengyou.vidio.config.WeChatConfig;
import com.pengyou.vidio.domain.JsonData;
import com.pengyou.vidio.domain.Video;
import com.pengyou.vidio.mapper.VideoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    @RequestMapping("/test")
    public String test(){

        return "hello video123123123112312312";
    }

    @Autowired
    private WeChatConfig weChatConfig;

    @RequestMapping("/test_config")
    public JsonData testConfig(){

        System.out.println(weChatConfig.getAppId());
        return JsonData.buildSuccess(weChatConfig.getAppId());
    }

    @Autowired(required = false)
    private VideoMapper videoMapper;

    @RequestMapping("/test_db")
    public Object testDB(){

        return videoMapper.findAll();
    }

}
