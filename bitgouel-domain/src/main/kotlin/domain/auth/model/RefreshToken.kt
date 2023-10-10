package domain.auth.model

import org.springframework.data.redis.core.RedisHash

@RedisHash("refresh_token")
class RefreshToken {
}