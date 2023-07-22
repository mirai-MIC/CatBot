package org.Simbot.aspect;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.event.GroupMessageEvent;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Aspect
@Slf4j
public class GroupFilterAspect {

    private final List<String> list;

    // 通过构造函数注入
    @Autowired
    public GroupFilterAspect(@Value("${group.whiteList}") final String groupWhiteList) {
        this.list = Arrays.asList(groupWhiteList.split(","));
    }

    @Around("@annotation(love.forte.simboot.annotation.Listener)")
    public Object around(final ProceedingJoinPoint joinPoint) throws Throwable {
        if (CollUtil.isEmpty(list)) {// 如果白名单为空，直接返回
            return joinPoint.proceed();
        }
        final Object[] args = joinPoint.getArgs();
        for (final Object arg : args) {
            if (arg instanceof final GroupMessageEvent event) {
                final String groupId = event.getGroup().getId().toString();
                if (list.contains(groupId)) {
                    log.info("groupId: {} is in the list", groupId);
                    return joinPoint.proceed();
                } else {
                    return null;  // 返回Null或者其它默认值
                }
            }
        }
        return joinPoint.proceed();
    }
}