package bitgouel.team.msg.global.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(
    basePackages = [
       "team.msg.domain",
    ]
)
class ComponentScanConfig