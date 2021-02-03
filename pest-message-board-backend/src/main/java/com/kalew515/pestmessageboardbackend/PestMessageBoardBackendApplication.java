package com.kalew515.pestmessageboardbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.kalew515.pestmessageboardbackend.mapper")
public class PestMessageBoardBackendApplication {

    public static void main (String[] args) {
        SpringApplication.run(PestMessageBoardBackendApplication.class, args);
    }

}
