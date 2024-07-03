package team.msg.domain.map.service

import team.msg.domain.map.presentstion.res.GetCoordinateResponseData

interface MapService {
    fun getCoordinate(address: String): GetCoordinateResponseData
}