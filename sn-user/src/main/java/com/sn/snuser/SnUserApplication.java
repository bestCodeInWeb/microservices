package com.sn.snuser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SnUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(SnUserApplication.class, args);
    }

}
