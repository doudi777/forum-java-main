package pub.developers.forum.portal.support;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import pub.developers.forum.common.support.RequestContext;

/**
 * @author xiongben
 * @create 23/12/23
 * @desc
 **/
@Component
@Aspect
public class ResponseBodyAspect {

    @Around("execution(* pub.developers.forum.portal..*.*(..)) && @annotation(postMapping)")
    public Object process(ProceedingJoinPoint joinPoint, PostMapping postMapping) throws Throwable {
        RequestContext.init();
        try {
            return joinPoint.proceed();
        } finally {
            RequestContext.removeAll();
        }
    }
}
