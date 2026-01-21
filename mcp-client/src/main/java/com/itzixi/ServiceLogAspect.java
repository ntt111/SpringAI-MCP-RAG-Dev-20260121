package com.itzixi;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
@Slf4j
@Aspect
public class ServiceLogAspect {
    /**
     *   Aop环绕切面 *返回任意类型 void ,也可以是其它类型的参数
     *   com.itzixi.service.impl 指定的包名，要切的class所在包
     *   .. 可以匹配到当前包和子包
     *   *  可以匹配到当前包和子包的class类
     *   .无意义
     *   *任意方法名
     *   (..)方法的参数，匹配任意参数
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("execution(* com.itzixi.service.impl..*.*(..))")
    public Object recordTime(ProceedingJoinPoint joinPoint) throws Throwable {
        //long begin = System.currentTimeMillis();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object proceed = joinPoint.proceed();
        stopWatch.stop();
        //long end = System.currentTimeMillis();
        String point = joinPoint.getTarget().getClass().getName()
                + "."
                + joinPoint.getSignature().getName();
        long time = stopWatch.getTotalTimeMillis();
        if(time>3000){
            log.error("打印 {} 耗时偏长 {} 毫秒", point, time);
        }else if (time>2000) {
            log.warn("打印 {} 耗时中等 {} 毫秒", point, time);
        }else {
            log.info("打印 {} 耗时 {} 毫秒", point, time);
        }
        return proceed;
    }
}
