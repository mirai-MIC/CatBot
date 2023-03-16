package org.Simbot.startup;

import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;
import love.forte.simbot.event.GroupMessageEvent;
import org.Simbot.mybatisplus.domain.PermissionsData;
import org.Simbot.mybatisplus.mapper.PermissionsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @BelongsProject: simbot
 * @BelongsPackage: org.Simbot.startup
 * @Author: MIC
 * @CreateTime: 2023-03-07  21:33
 * @Description:
 * @Version: 1.0
 */


@Component
public class TablesStarup {
    @Autowired
    private PermissionsMapper mapper;

    @Listener
    @Filter(value = "/开启功能")
    public void pluginStartup(GroupMessageEvent event) {
//        Boolean value = mapper.selectById(1).getValue();
        PermissionsData permissionsData = new PermissionsData();
        permissionsData.setId(1);
        permissionsData.setValue(1);

        mapper.updateById(permissionsData);
    }
}
