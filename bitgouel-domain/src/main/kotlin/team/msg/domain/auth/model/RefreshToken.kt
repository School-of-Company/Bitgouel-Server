package team.msg.domain.auth.model

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import team.msg.domain.user.enums.Authority
import java.util.UUID

@RedisHash("refresh_token")
class RefreshToken(
    @Id
    val token: String,

    val userId: UUID,

    val authority: Authority,

    @TimeToLive
    val expiredAt: Int
)