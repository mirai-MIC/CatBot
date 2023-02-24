package org.Simbot.plugins.yiyan;

import lombok.extern.slf4j.Slf4j;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;
import love.forte.simboot.filter.MatchType;
import love.forte.simbot.event.GroupMessageEvent;
import org.Simbot.utils.OK3HttpClient;
import org.Simbot.utils.SendMsgUtil;
import org.springframework.stereotype.Component;

/**
 * @BelongsProject: simbot
 * @BelongsPackage: org.Simbot.plugins.yiyan
 * @Author: MIC
 * @CreateTime: 2023-02-22  17:14
 * @Description:
 * @Version: 1.0
 */


@Component
@Slf4j
public class yiYan {
    @Filter(value = "/一言", matchType = MatchType.REGEX_MATCHES)
    @Listener
    public void sendYan(GroupMessageEvent event) {
        try {
            var textJson = OK3HttpClient.httpGet("https://ovooa.com/API/yiyan/api.php", null, null);
            SendMsgUtil.sendSimpleGroupMsg(event.getGroup(), "[随机一言]\n" + textJson);
        } catch (Exception e) {
            log.error("一言: " + e.getMessage());
            event.replyAsync(e.getMessage());
        }
    }
}
