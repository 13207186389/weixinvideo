package com.pengyou.vidio;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.pengyou.vidio.mapper")//为mapper接口创建代理对象
@EnableTransactionManagement//开启事务管理
public class VidioApplication {

    public static void main(String[] args) {
        SpringApplication.run(VidioApplication.class, args);
    }

}
