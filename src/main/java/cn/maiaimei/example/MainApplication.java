package cn.maiaimei.example;

import cn.maiaimei.framework.swift.annotation.EnableSwiftMT;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableSwiftMT
//@EnableSwiftMT7xx
@SpringBootApplication
public class MainApplication {
    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }
}
