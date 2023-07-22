package org.Simbot.startup;

import lombok.extern.slf4j.Slf4j;
import org.Simbot.utils.FormatTime;
import org.Simbot.utils.SendMsgUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

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
    @Value("${user.Master}")
    String master;

    @Override
    public void run(final ApplicationArguments args) {
        log.info("====================MusicCatBot启动成功=====================");
        SendMsgUtil.sendFriendMessage(Long.parseLong(master), "====MusicCatBot启动成功====\n时间: " + new FormatTime().getTimeSecond() + "\n功能预览: https://github.com/mirai-MIC/CatBot#readme");
    }
}
