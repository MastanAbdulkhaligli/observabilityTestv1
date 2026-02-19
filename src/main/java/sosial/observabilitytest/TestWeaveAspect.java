package sosial.observabilitytest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class TestWeaveAspect {

    @Around("execution(* sosial..*(..)) && !within(sosial.observabilitytest.TestWeaveAspect)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("[TEST-WEAVE] " + pjp.getSignature());
        return pjp.proceed();
    }
}
