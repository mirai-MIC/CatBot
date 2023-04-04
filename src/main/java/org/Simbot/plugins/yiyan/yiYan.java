package org.Simbot.plugins.yiyan;

import lombok.extern.slf4j.Slf4j;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;
import love.forte.simboot.filter.MatchType;
import love.forte.simbot.event.GroupMessageEvent;
import org.Simbot.utils.OK3HttpClient;
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

        OK3HttpClient.httpGetAsync("https://ovooa.com/API/yiyan/api.php", null, null,
                event::replyAsync,
                error -> {
                    log.error("调用三方接口出错", error);
                }
        );

    }
}
