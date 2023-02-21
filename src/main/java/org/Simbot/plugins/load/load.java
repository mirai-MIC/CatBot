package org.Simbot.plugins.load;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import love.forte.simboot.annotation.ContentTrim;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;
import love.forte.simbot.component.mirai.message.MiraiForwardMessageBuilder;
import love.forte.simbot.event.GroupMessageEvent;
import love.forte.simbot.message.MessagesBuilder;
import love.forte.simbot.resources.Resource;
import org.Simbot.mybatisplus.domain.loadData;
import org.Simbot.mybatisplus.mapper.LoadMapper;
import org.Simbot.plugins.load.LoadData.data;
import org.Simbot.utils.HttpUtils;
import org.Simbot.utils.Properties.properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

/**
 * @author mirai
 * @version 1.0
 * &#064;packAge:  org.Simbot.Plugins.Load
 * &#064;date  2022/12/7 13:31
 */
@Component
@Slf4j
public class load {
    @lombok.Getter
    @Deprecated
    String loadUrl = new properties().getProperties("cache/application.properties", "user.load");
    @Autowired
    private LoadMapper mapper;
    public load() throws IOException {
    }
    @Listener
    @ContentTrim
    @Filter(value = "抽签")
    public void getLoad(GroupMessageEvent event) {
        MiraiForwardMessageBuilder miraiForwardMessageBuilder = new MiraiForwardMessageBuilder();

        MessagesBuilder builder = new MessagesBuilder();
        builder.at(event.getAuthor().getId());
        builder.text("\n");
        try {
            if (getLoadUrl().isEmpty()) {
                log.info("抽签接口为空");
                event.replyAsync("接口为空");
            } else {
                data loadData = new Gson().fromJson(HttpUtils.get(getLoadUrl()), data.class);
                data.DataDTO getDTO = loadData.getData();
                if (loadData.getCode() == 1) {
                    loadData LoadData = new loadData();
                    LoadData.setId(Long.valueOf(event.getAuthor().getId().toString().trim()));
                    mapper.insert(LoadData);

                    builder.append("Format: " + getDTO.getFormat() + "\n");
                    builder.append("Draw: " + getDTO.getDraw() + "\n");
                    builder.append("Annotate: " + getDTO.getAnnotate() + "\n");
                    builder.append("Explain: " + getDTO.getExplain() + "\n");
                    builder.append("Details: " + getDTO.getDetails() + "\n");
                    builder.append("Source: " + getDTO.getSource() + "\n");
                    builder.image(Resource.of(new URL(getDTO.getImage())));
                    miraiForwardMessageBuilder.add(event.getAuthor().getId(), event.getAuthor().getNickOrUsername(), builder.build());
                    event.getSource().sendBlocking(miraiForwardMessageBuilder.build());
                } else {
                    event.replyAsync("接口异常");
                }
            }
        } catch (Exception e) {
            event.replyAsync("今天已经抽过了");
            log.info("写入重复，已签到");
        }
    }
}
