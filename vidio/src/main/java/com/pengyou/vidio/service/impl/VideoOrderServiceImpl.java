package com.pengyou.vidio.service.impl;


import com.pengyou.vidio.config.WeChatConfig;
import com.pengyou.vidio.domain.User;
import com.pengyou.vidio.domain.Video;
import com.pengyou.vidio.domain.VideoOrder;
import com.pengyou.vidio.dto.VideoOrderDto;
import com.pengyou.vidio.mapper.UserMapper;
import com.pengyou.vidio.mapper.VideoMapper;
import com.pengyou.vidio.mapper.VideoOrderMapper;
import com.pengyou.vidio.service.VideoOrderService;
import com.pengyou.vidio.utils.CommonUtils;
import com.pengyou.vidio.utils.HttpUtils;
import com.pengyou.vidio.utils.WXPayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

@Service
public class VideoOrderServiceImpl implements VideoOrderService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Logger dataLogger = LoggerFactory.getLogger("dataLogger");


    @Autowired
    private WeChatConfig weChatConfig;

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private VideoOrderMapper videoOrderMapper;

    @Autowired
    private UserMapper userMapper;

    @Override       //隔离级别
    @Transactional(propagation = Propagation.REQUIRED)
    public String save(VideoOrderDto videoOrderDto) throws Exception{

        dataLogger.info("module=video_order`api=save`user_id={}`video_id={}",videoOrderDto.getUserId(),videoOrderDto.getVideoId());
        logger.info("1234567894561234567894651321456789");

        //查找视频信息
        Video video =  videoMapper.findById(videoOrderDto.getVideoId());

        //查找用户信息
        User user = userMapper.findByid(videoOrderDto.getUserId());

        //生成订单
        VideoOrder videoOrder = new VideoOrder();
        videoOrder.setTotalFee(video.getPrice());
        videoOrder.setVideoImg(video.getCoverImg());
        videoOrder.setVideoTitle(video.getTitle());
        videoOrder.setCreateTime(new Date());
        videoOrder.setVideoId(video.getId());
        videoOrder.setState(0);
        videoOrder.setUserId(user.getId());
        videoOrder.setHeadImg(user.getHeadImg());
        videoOrder.setNickname(user.getName());
        videoOrder.setDel(0);
        videoOrder.setIp(videoOrderDto.getIp());
        videoOrder.setOutTradeNo(CommonUtils.generateUUID());
        videoOrderMapper.insert(videoOrder);

        //统一下单,生成签名,获取codeurl并返回
        String codeUrl=unifiedOrder(videoOrder);

        return codeUrl;
    }

    @Override
    public VideoOrder findByOutTradeNo(String outTradeNo) {

        return videoOrderMapper.findByOutTradeNo(outTradeNo);
    }

    @Override
    public int updateVideoOderByOutTradeNo(VideoOrder videoOrder) {
        return videoOrderMapper.updateVideoOderByOutTradeNo(videoOrder);
    }


    /**
     * 统一下单方法
     * @return
     */
    private String unifiedOrder(VideoOrder videoOrder) throws Exception {

        //生成签名
        SortedMap<String,String> params = new TreeMap<>();
        params.put("appid",weChatConfig.getAppId());
        params.put("mch_id", weChatConfig.getMchId());
        params.put("nonce_str",CommonUtils.generateUUID());
        params.put("body",videoOrder.getVideoTitle());
        params.put("out_trade_no",videoOrder.getOutTradeNo());
        params.put("total_fee",videoOrder.getTotalFee().toString());
        params.put("spbill_create_ip",videoOrder.getIp());
        params.put("notify_url",weChatConfig.getPayCallbackUrl());
        params.put("trade_type","NATIVE");

        //sign签名
        String sign = WXPayUtil.createSign(params, weChatConfig.getKey());
        params.put("sign",sign);

        //map转xml
        String payXml = WXPayUtil.mapToXml(params);
        String url=WeChatConfig.getUnifiedOrderUrl();
        //统一下单 返回一个xml文本
        String orderStr=HttpUtils.doPost(WeChatConfig.getUnifiedOrderUrl(),payXml,4000);
        if(null == orderStr) {
            return null;
        }

        //把返回的xml转换成一个Map
        Map<String, String> unifiedOrderMap =  WXPayUtil.xmlToMap(orderStr);
        System.out.println(unifiedOrderMap.toString());
        if(unifiedOrderMap!=null){
            return unifiedOrderMap.get("code_url");
        }



        return null;
    }

}
