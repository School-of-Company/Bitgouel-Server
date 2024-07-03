package team.msg.common.util

import org.springframework.stereotype.Component
import team.msg.domain.map.AddressNotFoundException
import team.msg.thirdparty.kakao.client.KakaoClient

@Component
class KakaoUtil(
    private val kakaoClient: KakaoClient
) {
    /**
     * 도로번/지번 주소로 좌표를 가져오는 비지니스 로직입니다.
     *
     * @param query 도로번/지번 주소
     * @return 좌표 X, Y 값
     */
    fun addressToXY(query: String): Pair<String, String> {
        val response = kakaoClient.getKakaoMapAddress(query)

        if(response.document.isEmpty())
            throw AddressNotFoundException("도로명/지번 주소를 찾을 수 없습니다. info : [ query = $query ]")

        val address = response.document.first()

        return Pair(address.x, address.y)
    }
}