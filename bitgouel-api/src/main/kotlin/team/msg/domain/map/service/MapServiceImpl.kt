package team.msg.domain.map.service

import org.springframework.stereotype.Service
import team.msg.common.util.KakaoUtil
import team.msg.domain.map.presentstion.res.GetCoordinateResponseData
import team.msg.domain.map.presentstion.res.MapResponseData

@Service
class MapServiceImpl(
    private val kakaoUtil: KakaoUtil
) : MapService {
    override fun getCoordinate(address: String): GetCoordinateResponseData {
        val coordinate = kakaoUtil.getCoordinate(address)
        val response = MapResponseData.of(coordinate)

        return response
    }
}