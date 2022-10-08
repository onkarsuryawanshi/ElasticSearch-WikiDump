package com.ex.springdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
@AutoConfiguration
public class Application {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(Application.class, args);
        System.out.println("hello");
    }

}
