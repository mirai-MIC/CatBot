package org.Simbot.plugins.tu;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;
import love.forte.simbot.event.GroupMessageEvent;
import love.forte.simbot.message.MessagesBuilder;
import love.forte.simbot.resources.Resource;
import org.Simbot.mybatisplusutils.domain.loadData;
import org.Simbot.mybatisplusutils.mapper.AliciaMapper;
import org.Simbot.mybatisplusutils.mapper.LoadMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;

/**
 * @author mirai
 * @version 1.0
 * @packAge: org.Simbot.Plugins.Tu
 * @date 2022/12/10 20:40
 * @description 发送图片
 */
@Slf4j
@Controller
public class GetTu {
    @Autowired
    private AliciaMapper mapper;

    @Autowired
    private LoadMapper loadMapper;

    @Filter(value = "/all")
    @Listener
    public void randomImage(GroupMessageEvent event) throws IOException {
        int randomIndex = (int) (Math.random() * mapper.selectCount(Wrappers.emptyWrapper()));
        MessagesBuilder messagesBuilder = new MessagesBuilder();
        messagesBuilder.at(event.getAuthor().getId());
        messagesBuilder.append("\nId: " + randomIndex + "\n");
        messagesBuilder.image(Resource.of(new URL(mapper.selectById(randomIndex).getUrl())));
        event.getSource().sendBlocking(messagesBuilder.build());
    }


    @Filter(value = "test")
    @Listener
    public void load(GroupMessageEvent event) {

        loadData loadData = new loadData();
        loadData.setId(111111L);
        loadMapper.insert(loadData);

    }


}