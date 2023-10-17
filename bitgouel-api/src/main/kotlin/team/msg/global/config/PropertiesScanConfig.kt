package team.msg.global.config

import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import team.msg.global.security.jwt.properties.JwtProperties

@Configuration
@ComponentScan(
    basePackages = ["team.msg"]
)
@ConfigurationPropertiesScan(
    basePackageClasses = [
        JwtProperties::class
    ]
)
class PropertiesScanConfig