package team.msg.common.annotation

import java.util.concurrent.TimeUnit

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class DistributedLock(
    val key: String,
    val leaseTime: Long = 10L,
    val waitTime: Long = 5L,
    val timeUnit: TimeUnit = TimeUnit.SECONDS
)
