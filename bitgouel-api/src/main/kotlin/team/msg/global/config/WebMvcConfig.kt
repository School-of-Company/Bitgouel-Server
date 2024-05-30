package team.msg.global.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfig : WebMvcConfigurer {

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedMethods("GET", "POST", "PUT", "PATCH", "HEAD", "DELETE", "OPTIONS")
            .allowedOrigins(
                "http://localhost:3000",
                "https://xn--299a9h0c783fmff8wewxc6hq8rfa3165a.com",
                "https://빛고을직업교육혁신지구.com"
            )
            .allowCredentials(true)
    }
}