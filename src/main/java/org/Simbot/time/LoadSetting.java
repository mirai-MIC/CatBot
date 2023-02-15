package org.Simbot.time;

import lombok.extern.slf4j.Slf4j;
import org.Simbot.db.dbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

/**
 * @author mirai
 * @version 1.0
 * @packAge: org.Simbot.time
 * @date 2022/12/7 20:16
 */
@Slf4j
@Controller
public class LoadSetting {
    @Autowired
    private dbUtils getDb;

    @Scheduled(cron = "0 0 0 * * ? ")
    public void getDelLoad() {
        getDb.clearLoad();
        log.info("清空成功");
    }
}
