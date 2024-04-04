package team.msg.global.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("spring.redis")
class RedisProperties(
    val host: String = "localhost",
    val port: String = "6379"
)