package team.ubox.starry.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import team.ubox.starry.service.dto.CustomResponse;

@Component
@Aspect
@Slf4j
public class ControllerAspect {
    @Around("execution(* team.ubox.starry.controller.*.*(..))")
    public Object httpLogging(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        log.info("[Req] From={}, Pathname={}", request.getRemoteAddr(), joinPoint.getSignature().getName());
        Object proceeded = null;
        try {
            proceeded = joinPoint.proceed();
        } catch (Exception e) {
            log.debug("[Exception] {}", e.getMessage());
            throw e;
        } finally {
            if(proceeded instanceof CustomResponse<?> customResponse) {
                log.info("[Res] From={}, Pathname={}, code={}, message={}", request.getRemoteAddr(), joinPoint.getSignature().getName(), customResponse.getCode(), customResponse.getMessage());
            } else {
                log.info("[Res] From={}, Pathname={}", request.getRemoteAddr(), joinPoint.getSignature().getName());
            }
        }
        return proceeded;
    }
}
