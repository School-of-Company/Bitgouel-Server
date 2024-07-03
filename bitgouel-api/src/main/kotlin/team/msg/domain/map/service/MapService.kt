package team.msg.domain.map.service

import team.msg.domain.map.presentation.response.GetCoordinateResponseData

interface MapService {
    fun getCoordinate(address: String): GetCoordinateResponseData
}