package com.pengyou.vidio;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.pengyou.vidio.mapper")//为mapper接口创建代理对象
public class VidioApplication {

    public static void main(String[] args) {
        SpringApplication.run(VidioApplication.class, args);
    }

}
