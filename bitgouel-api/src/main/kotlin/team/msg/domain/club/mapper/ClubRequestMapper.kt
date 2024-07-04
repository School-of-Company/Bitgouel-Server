package team.msg.domain.club.mapper

import team.msg.domain.club.presentation.data.reqeust.CreateClubRequest
import team.msg.domain.club.presentation.data.reqeust.UpdateClubRequest
import team.msg.domain.club.presentation.web.request.CreateClubWebRequest
import team.msg.domain.club.presentation.web.request.UpdateClubWebRequest

interface ClubRequestMapper {
    fun createClubWebRequestToDto(webRequest: CreateClubWebRequest): CreateClubRequest
    fun updateClubWebRequestToDto(webRequest: UpdateClubWebRequest): UpdateClubRequest
}