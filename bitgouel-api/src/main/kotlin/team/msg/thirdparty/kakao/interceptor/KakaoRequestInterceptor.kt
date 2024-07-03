package team.msg.thirdparty.kakao.interceptor

import feign.RequestInterceptor
import feign.RequestTemplate
import team.msg.thirdparty.kakao.properties.KakaoProperties

class KakaoRequestInterceptor(
    private val kakaoProperties: KakaoProperties
) : RequestInterceptor {

    override fun apply(p0: RequestTemplate?) {
        p0?.header("Authorization", "KakaoAK ${kakaoProperties.apiKey}")
    }

}