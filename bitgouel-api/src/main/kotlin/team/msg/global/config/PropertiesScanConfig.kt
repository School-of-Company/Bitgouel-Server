package team.msg.global.config

import team.msg.global.security.jwt.properties.JwtProperties
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.Configuration
import team.msg.global.config.properties.EmailProperties
import team.msg.global.config.properties.RedisProperties
import team.msg.thirdparty.aws.AwsProperties
import team.msg.thirdparty.aws.storage.properties.AwsS3Properties

@Configuration
@ConfigurationPropertiesScan(
    basePackageClasses = [
        JwtProperties::class,
        EmailProperties::class,
        RedisProperties::class,
        AwsProperties::class,
        AwsS3Properties::class
    ]
)
class PropertiesScanConfig