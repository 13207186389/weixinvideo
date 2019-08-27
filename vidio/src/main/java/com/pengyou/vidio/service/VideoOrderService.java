package com.pengyou.vidio.service;


import com.pengyou.vidio.domain.VideoOrder;
import com.pengyou.vidio.dto.VideoOrderDto;

/**
 * 订单接口
 */
public interface VideoOrderService {

    /**
     * 下单接口
     * @param videoOrderDto
     * @return
     */
    VideoOrder save(VideoOrderDto videoOrderDto) throws Exception;

}
