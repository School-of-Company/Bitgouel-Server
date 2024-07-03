package team.msg.thirdparty.feign.kakao.client.data.res

/**
 * @param mata 응답 관련 정보
 * @param document 응답 결과
 */
data class KakaoAddressResponseData(
    val mata: Meta,
    val document: List<Document>
)
