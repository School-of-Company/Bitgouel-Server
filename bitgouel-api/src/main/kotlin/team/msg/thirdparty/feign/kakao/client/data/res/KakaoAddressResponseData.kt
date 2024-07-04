package team.msg.thirdparty.feign.kakao.client.data.res

/**
 * @param meta 응답 관련 정보
 * @param documents 응답 결과
 */
data class KakaoAddressResponseData(
    val meta: Meta,
    val documents: List<Document>
)
