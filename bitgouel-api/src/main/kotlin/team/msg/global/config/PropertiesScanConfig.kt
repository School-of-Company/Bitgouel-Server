package team.msg.global.config

import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.Configuration
import team.msg.global.config.properties.EmailProperties
import team.msg.global.config.properties.RedisProperties
import team.msg.global.security.jwt.properties.JwtProperties
import team.msg.thirdparty.aws.AwsProperties
import team.msg.thirdparty.kakao.properties.KakaoProperties

@Configuration
@ConfigurationPropertiesScan(
    basePackageClasses = [
        JwtProperties::class,
        EmailProperties::class,
        RedisProperties::class,
        AwsProperties::class,
        KakaoProperties::class
    ]
)
class PropertiesScanConfig