package team.msg.common.aop.lock

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.redisson.api.RedissonClient
import org.springframework.expression.EvaluationContext
import org.springframework.expression.ExpressionParser
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext
import org.springframework.stereotype.Component
import team.msg.common.annotation.DistributedLock
import team.msg.global.exception.LockInterruptedException
import java.time.LocalDateTime
import java.util.*


@Aspect
@Component
class DistributedLockAspect(
    private val redissonClient: RedissonClient,
    private val transactionAspect: TransactionAspect
) {
    @Around("@annotation(team.msg.common.annotation.DistributedLock)")
    fun getLock(proceedingJoinPoint: ProceedingJoinPoint): Any? {
        val signature = proceedingJoinPoint.signature as MethodSignature
        val annotation = signature.method.getAnnotation(DistributedLock::class.java)
        val keyName = getDynamicValue(signature.parameterNames, proceedingJoinPoint.args, annotation.key) as UUID
        val rLock = redissonClient.getLock("$keyName-${LocalDateTime.now()}")

        try {
            rLock.tryLock(annotation.waitTime, annotation.leaseTime, annotation.timeUnit)
            return transactionAspect.proceed(proceedingJoinPoint)

        } catch(e: InterruptedException) {
            throw LockInterruptedException("락 획득에 실패하였습니다. info : [ lockName = ${annotation.key} ]")
        } finally {
            if(rLock.isLocked)
                rLock.unlock()
        }
    }

    fun getDynamicValue(parameterNames: Array<String>, args: Array<Any?>, name: String): Any? {
        val parser: ExpressionParser = SpelExpressionParser()
        val context: EvaluationContext = StandardEvaluationContext()

        for (i in parameterNames.indices) {
            context.setVariable(parameterNames[i], args[i])
        }
        return parser.parseExpression(name).getValue(context)
    }
}