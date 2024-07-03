package team.msg.thirdparty.kakao.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import team.msg.thirdparty.kakao.interceptor.KakaoRequestInterceptor
import team.msg.thirdparty.kakao.properties.KakaoProperties

@Configuration
class KakaoFeignConfig(
    private val kakaoProperties: KakaoProperties
) {
    @Bean
    fun kakaoFeignRequestInterceptor(): KakaoRequestInterceptor =
        KakaoRequestInterceptor(kakaoProperties)
}