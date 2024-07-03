package team.msg.thirdparty.kakao.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "kakao")
@ConstructorBinding
class KakaoProperties(
    val apiKey: String
)