package org.Simbot;


import love.forte.simboot.spring.autoconfigure.EnableSimbot;
import net.mamoe.mirai.utils.BotConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import xyz.cssxsh.mirai.tool.FixProtocolVersion;


@EnableSimbot
@EnableScheduling
@SpringBootApplication
@EnableAspectJAutoProxy
@EnableAsync
public class SimbotApp {
    public static void main(final String... args) {
//        FixProtocolVersion.update();
        //自建协议服务器
        FixProtocolVersion.fetch(BotConfiguration.MiraiProtocol.ANDROID_PAD, "8.9.58");
        SpringApplication.run(SimbotApp.class, args);

    }
}