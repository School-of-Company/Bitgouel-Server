package team.msg.domain.map.service

import org.springframework.stereotype.Service
import team.msg.common.util.KakaoUtil
import team.msg.domain.map.presentation.response.GetCoordinateResponseData
import team.msg.domain.map.presentation.response.MapResponse

@Service
class MapServiceImpl(
    private val kakaoUtil: KakaoUtil
) : MapService {
    override fun getCoordinate(address: String): GetCoordinateResponseData {
        val coordinate = kakaoUtil.getCoordinate(address)
        val response = MapResponse.of(coordinate)

        return response
    }
}