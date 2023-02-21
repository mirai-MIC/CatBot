package org.Simbot.plugins.menu;

import lombok.extern.slf4j.Slf4j;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;
import love.forte.simboot.filter.MatchType;
import love.forte.simbot.event.GroupMessageEvent;
import org.Simbot.utils.Properties.properties;
import org.Simbot.utils.SendMsgUtil;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @BelongsProject: simbot
 * @BelongsPackage: org.Simbot.plugins.menu
 * @Author: MIC
 * @CreateTime: 2023-02-21  10:55
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Component
public class menuImage {

    @lombok.Getter
    @Deprecated
    String image = new properties().getProperties("cache/application.properties", "bot.menu");

    public menuImage() throws IOException {
    }

    @Listener
    @Filter(value = "/菜单", matchType = MatchType.TEXT_EQUALS)
    public void sendMenu(GroupMessageEvent event) {
        SendMsgUtil.sendSimpleGroupImage(event.getGroup(), event.getAuthor().getId(), "菜单在这哟~~", getImage());
    }
}
