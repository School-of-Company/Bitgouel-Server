package team.msg.global.config

import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.client.codec.StringCodec
import org.redisson.config.Config
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import team.msg.global.config.properties.RedisProperties

@Configuration
class RedissonConfig(
    private val redisProperties: RedisProperties
) {
    companion object {
        const val REDIS_PREFIX= "redis://"
    }

    @Bean
    fun redissonClient(): RedissonClient {
        val config = Config()
        val codec = StringCodec()
        config.codec = codec

        config.useSingleServer()
            .setAddress(
                "$REDIS_PREFIX${redisProperties.host}:${redisProperties.port}"
            )
            .setConnectionPoolSize(20)
            .setConnectionMinimumIdleSize(5)
        return Redisson.create(config)
    }
}