package team.msg.domain.map.service

import org.springframework.stereotype.Service
import team.msg.common.util.KakaoUtil
import team.msg.domain.map.presentation.response.GetCoordinateResponse
import team.msg.domain.map.presentation.response.MapResponse

@Service
class MapServiceImpl(
    private val kakaoUtil: KakaoUtil
) : MapService {
    override fun getCoordinate(address: String): GetCoordinateResponse {
        val coordinate = kakaoUtil.getCoordinate(address)
        val response = MapResponse.of(coordinate)

        return response
    }
}