package org.Simbot.config.caffeine;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.concurrent.TimeUnit;

@Data
@AllArgsConstructor
public class CacheDataWrapper {
    //缓存内容
    private Object data;
    //缓存过期时间
    private long delay;
    //缓存过期时间单位
    private TimeUnit unit;
}
