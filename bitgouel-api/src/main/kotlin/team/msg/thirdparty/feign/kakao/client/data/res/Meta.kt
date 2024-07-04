package team.msg.thirdparty.feign.kakao.client.data.res

/**
 * @param total 검색어에 검색된 문서 수
 * @param pageableCount total_count 중 노출 가능 문서 수
 *  (최대: 45)
 * @param isEnd 현재 페이지가 마지막 페이지인지 여부
 *  값이 false면 다음 요청 시 page 값을 증가시켜 다음 페이지 요청 가능
 */
data class Meta(
    val total: Int,
    val pageableCount: Int,
    val isEnd: Boolean
)
