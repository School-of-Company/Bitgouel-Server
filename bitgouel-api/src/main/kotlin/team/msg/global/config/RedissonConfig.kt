package team.msg.global.config

import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.client.codec.StringCodec
import org.redisson.config.Config
import org.springframework.context.annotation.Configuration

@Configuration
class RedissonConfig(
    private val redisConfig: RedisConfig
) {
    companion object {
        const val REDIS_PREFIX= "redis://"
    }

    fun createRedissonClient(): RedissonClient {
        val config = Config()
        val codec = StringCodec()
        config.codec = codec
        config.useSingleServer().setAddress(
            "$REDIS_PREFIX${redisConfig.host}:${redisConfig.port}"
        )
        return Redisson.create(config)
    }
}