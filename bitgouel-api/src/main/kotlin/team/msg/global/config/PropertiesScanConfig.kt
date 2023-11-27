package team.msg.global.config

import team.msg.global.security.jwt.properties.JwtProperties
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationPropertiesScan(
    basePackageClasses = [
        JwtProperties::class
    ]
)
class PropertiesScanConfig