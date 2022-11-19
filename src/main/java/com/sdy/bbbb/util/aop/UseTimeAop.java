package com.sdy.bbbb.util.aop;

import org.apache.catalina.security.SecurityUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class UseTimeAop {

    //어드바이스의 동작 시점
    //@Around = @Before(호출 전) + @After(호출 후(성패 여부X)), @AfterReturning (호출에 성공했을 때), @AfterThrowing (예외가 발생했을 때)
    //@Around("@annotation(어노테이션 이름)")
    @Around("execution(public * com.sdy.bbbb.controller..*(..))")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        // 측정 시작 시간
        long startTime = System.currentTimeMillis();

        try {
            // 핵심기능 수행 (쪼인포인트 > 포인트 컷)
            Object output = joinPoint.proceed();
            return output;
        } finally {
            // 측정 종료 시간
            long endTime = System.currentTimeMillis();
            // 수행시간 = 종료 시간 - 시작 시간
            long runTime = endTime - startTime;
            System.out.println("======================================[API Use Time] UserId: " + ", Total Time: " + runTime + " ms");
        }
    }
}
