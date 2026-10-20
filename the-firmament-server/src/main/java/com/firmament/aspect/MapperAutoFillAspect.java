package com.firmament.aspect;

import com.firmament.annotation.MapperAutoFill;
import com.firmament.constant.AutoFillConstant;
import com.firmament.context.BaseContext;
import com.firmament.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 切面类，向需要自动填充字段的方法内注入
 */
@Aspect
@Component
@Slf4j
public class MapperAutoFillAspect {

    //切入点表达式方法
    @Pointcut("execution(* com.firmament.mapper.*.*(..)) && @annotation(com.firmament.annotation.MapperAutoFill)")
    public void autoFillPointCut(){}

    //通知
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        log.info("进行公共字段自动填充");

        // 获得操作类型
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        MapperAutoFill mapperAutoFill = signature.getMethod().getAnnotation(MapperAutoFill.class);
        OperationType operationType = mapperAutoFill.value();

        //获得方法参数列表
        Object[] args = joinPoint.getArgs();

        if(args == null || args.length == 0 ){
            return;
        }else{
            Object entity = args[0];

            LocalDateTime nowTime = LocalDateTime.now();
            Long nowUser = BaseContext.getCurrentId();

            if(operationType == OperationType.INSERT){
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                setCreateTime.invoke(entity,nowTime);
                setUpdateTime.invoke(entity,nowTime);
                setCreateUser.invoke(entity,nowUser);
                setUpdateUser.invoke(entity,nowUser);

                return;

            } else if (operationType == OperationType.UPDATE) {
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                setUpdateTime.invoke(entity,nowTime);
                setUpdateUser.invoke(entity,nowUser);

                return;
            }
        }
    }

}
