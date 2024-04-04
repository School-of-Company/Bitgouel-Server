package team.msg.common.aop.lock

import org.aspectj.lang.ProceedingJoinPoint
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Component
class TransactionAspect {
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun proceed(proceedingJoinPoint: ProceedingJoinPoint): Any? {
        return proceedingJoinPoint.proceed()
    }
}