package org.Simbot.startup;

import lombok.extern.slf4j.Slf4j;
import org.Simbot.utils.FormatTime;
import org.Simbot.utils.Properties.properties;
import org.Simbot.utils.SendMsgUtil;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @BelongsProject: simbot
 * @BelongsPackage: org.Simbot.startup
 * @Author: MIC
 * @CreateTime: 2023-02-16  15:18
 * @Description: Bot启动后自动向主人账号发送消息
 * @Version: 1.0
 */

@Component
@Slf4j
public class start implements ApplicationRunner {
    @lombok.Getter
    @Deprecated
    String master = new properties().getProperties("cache/application.properties", "user.Master");
    public start() throws IOException {
    }
    @Override
    public void run(ApplicationArguments args) {
        log.info("====================MusicCatBot启动成功=====================");
        SendMsgUtil.sendFriendMessage(Long.parseLong(getMaster()), "====MusicCatBot启动成功====\n时间: " + new FormatTime().getTimeSecond() + "\n功能预览: https://github.com/mirai-MIC/CatBot#readme");
    }
}
