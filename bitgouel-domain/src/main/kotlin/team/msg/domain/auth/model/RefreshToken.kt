package team.msg.domain.auth.model

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import team.msg.domain.user.enums.Authority
import java.util.UUID
import java.util.concurrent.TimeUnit

@RedisHash("refresh_token")
data class RefreshToken(
    @Id
    val token: String,

    val userId: UUID,

    val authority: Authority,

    @TimeToLive(unit = TimeUnit.SECONDS)
    val expiredAt: Int
)