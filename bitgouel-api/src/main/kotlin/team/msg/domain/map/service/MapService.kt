package team.msg.domain.map.service

import team.msg.domain.map.presentation.response.GetCoordinateResponse

interface MapService {
    fun getCoordinate(address: String): GetCoordinateResponse
}