package team.msg.thirdparty.feign.config

import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Configuration

@EnableFeignClients(basePackages = ["team.msg.thirdparty.feign"])
@Configuration
class FeignConfig