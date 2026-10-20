package com.firmament.annotation;

import com.firmament.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识方法需要某字段自动填充，如更新时间
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MapperAutoFill {

    //操作枚举，UPDATE/INSERT
    OperationType value();



}
