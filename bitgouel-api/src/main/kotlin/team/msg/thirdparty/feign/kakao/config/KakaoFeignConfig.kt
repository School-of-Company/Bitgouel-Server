package team.msg.thirdparty.feign.kakao.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import team.msg.thirdparty.feign.kakao.interceptor.KakaoRequestInterceptor
import team.msg.thirdparty.feign.kakao.properties.KakaoProperties

@Configuration
class KakaoFeignConfig(
    private val kakaoProperties: KakaoProperties
) {
    @Bean
    fun kakaoFeignRequestInterceptor(): KakaoRequestInterceptor =
        KakaoRequestInterceptor(kakaoProperties)
}