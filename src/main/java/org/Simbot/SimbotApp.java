package org.Simbot;


import love.forte.simboot.spring.autoconfigure.EnableSimbot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableSimbot
@EnableScheduling
@SpringBootApplication
@EnableAspectJAutoProxy
public class SimbotApp {
    public static void main(String... args) {
        SpringApplication.run(SimbotApp.class, args);
    }
}