package team.msg.domain.email.model

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash

@RedisHash("email_authentication", timeToLive = 60 * 5)
data class EmailAuthentication(
    @Id
    val email: String,
    val code: String,
    val isAuthentication: Boolean,
    val attemptCount: Int = 0
)
