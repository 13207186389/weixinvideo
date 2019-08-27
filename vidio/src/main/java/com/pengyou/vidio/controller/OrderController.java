package com.pengyou.vidio.controller;


import com.pengyou.vidio.domain.JsonData;
import com.pengyou.vidio.dto.VideoOrderDto;
import com.pengyou.vidio.service.VideoOrderService;
import com.pengyou.vidio.utils.IpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 订单接口
 */
@RestController
//@RequestMapping("/user/api/v1/order")
@RequestMapping("/api/v1/order")
public class OrderController {

    @Autowired
    private VideoOrderService videoOrderService;

    @GetMapping("add")
    public JsonData saveOrder(@RequestParam(value = "video_id",required = true)int videoId,
                              HttpServletRequest request)throws Exception{

        //String ip = IpUtils.getIpAddr(request);
        String ip="120.25.1.43";//临时写死
        //int userId = request.getAttribute("user_id");
        int userId = 1; //临时写死的
        VideoOrderDto videoOrderDto = new VideoOrderDto();
        videoOrderDto.setUserId(userId);
        videoOrderDto.setVideoId(videoId);
        videoOrderDto.setIp(ip);

        videoOrderService.save(videoOrderDto);


        return JsonData.buildSuccess("下单成功");
    }




}
