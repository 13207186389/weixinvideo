package com.pengyou.vidio.controller;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.pengyou.vidio.domain.JsonData;
import com.pengyou.vidio.dto.VideoOrderDto;
import com.pengyou.vidio.service.VideoOrderService;
import com.pengyou.vidio.utils.IpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 订单接口
 */
@RestController
@RequestMapping("/user/api/v1/order")
//@RequestMapping("/api/v1/order")
public class OrderController {

    @Autowired
    private VideoOrderService videoOrderService;

    @GetMapping("add")
    public void saveOrder(@RequestParam(value = "video_id",required = true)int videoId,
                              HttpServletRequest request, HttpServletResponse response)throws Exception{

        String ip = IpUtils.getIpAddr(request);
        //String ip="120.25.1.43";//临时写死
        int userId = (Integer) request.getAttribute("user_id");
        //int userId = 1; //临时写死的
        VideoOrderDto videoOrderDto = new VideoOrderDto();
        videoOrderDto.setUserId(userId);
        videoOrderDto.setVideoId(videoId);
        videoOrderDto.setIp(ip);

        //生成订单并返回微信支付codeUrl
        String codeUrl=videoOrderService.save(videoOrderDto);

        if(codeUrl == null) {
            throw new  NullPointerException();
        }

        try {

            //生成二维码配置
            Map<EncodeHintType,Object> hints =  new HashMap<>();

            //设置纠错等级
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

            //编码类型
            hints.put(EncodeHintType.CHARACTER_SET,"UTF-8");

            //构造一个图片对象
            BitMatrix bitMatrix = new MultiFormatWriter().encode(codeUrl, BarcodeFormat.QR_CODE,400,400,hints);

            //先获得输出流
            OutputStream out=response.getOutputStream();

            //通过工具类写出去
            MatrixToImageWriter.writeToStream(bitMatrix,"png",out);

        }catch (Exception e){
            e.printStackTrace();
        }

    }




}
