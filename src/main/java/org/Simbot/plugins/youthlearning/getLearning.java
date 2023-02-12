package org.Simbot.plugins.youthlearning;

import com.google.gson.Gson;
import love.forte.simboot.annotation.ContentTrim;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;
import love.forte.simbot.event.GroupMessageEvent;
import love.forte.simbot.message.MessagesBuilder;
import love.forte.simbot.resources.Resource;
import org.Simbot.plugins.youthlearning.Data.data;
import org.Simbot.utils.HttpUtils;
import org.Simbot.utils.Properties.properties;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author mirai
 * @version 1.0
 * @packAge: org.Simbot.Plugins.youthBigLearning
 * @date 2022/12/6 20:16
 */
@Component
public class getLearning {
    @lombok.Getter
    @Deprecated
    String Lear = new properties().getProperties("cache/application.properties", "user.lear");

    public getLearning() throws IOException {
    }


    @Listener
    @ContentTrim
    @Filter(value = "/青年大学习")
    public void getBigLearning(GroupMessageEvent event) throws MalformedURLException {
        MessagesBuilder builder = new MessagesBuilder();
        data gsonData = new Gson().fromJson(HttpUtils.get(getLear()), data.class);
        builder.at(event.getAuthor().getId());
        data.DataDTO gsonDataData = gsonData.getData();
        builder.text("\n标题: " + gsonDataData.getTitle() + "\n");
        builder.text("主页: " + gsonDataData.getUrl() + "\n");
        builder.image(Resource.of(new URL(gsonDataData.getImg())));
        event.getSource().sendBlocking(builder.build());

    }
}
