package org.Simbot.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @BelongsPackage: org.Simbot.config
 * @Author: MIC
 * @CreateTime: 2023-03-07  19:48
 * @Version: 1.0
 */


@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FunctionTaget {
    boolean value() default false;
}
