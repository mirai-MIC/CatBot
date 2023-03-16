package org.Simbot.annotations;

import org.Simbot.config.FunctionTaget;
import org.Simbot.mybatisplus.mapper.PermissionsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

/**
 * @BelongsProject: simbot
 * @BelongsPackage: org.Simbot.annotations
 * @Author: MIC
 * @CreateTime: 2023-03-07  19:53
 * @Description:
 * @Version: 1.0
 */
@Component
public class Cust implements FunctionTaget {


    @Autowired
    private PermissionsMapper permissionsMapper;

    @Override
    public boolean value() {
        if (permissionsMapper.selectById(1).getValue() != 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}
