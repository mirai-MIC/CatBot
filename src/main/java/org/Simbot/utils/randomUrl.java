package org.Simbot.utils;

import java.util.Random;

/**
 * @author mirai
 * @className randomUrl
 * @data 2023/01/12 21:23
 * @description
 */
public class randomUrl {
    public String getArrUrlGet() {
        String[] arrUrl = {"https://xiaobai.klizi.cn/API/img/beauty.php?data=&", "http://api.xn--7gqa009h.top/api/sjtp2?n=1"};
        return arrUrl[new Random().nextInt(arrUrl.length)];
    }

    public String getArrUrl() {
        String[] arrUrl = {"https://api.xiaobaibk.com/api/pic/?pic=girl"};
        return arrUrl[new Random().nextInt(arrUrl.length)];
    }
}
