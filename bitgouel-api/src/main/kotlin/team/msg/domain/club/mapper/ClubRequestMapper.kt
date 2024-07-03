package team.msg.domain.club.mapper

import team.msg.domain.club.presentation.data.reqeust.CreateClubRequest
import team.msg.domain.club.presentation.web.request.CreateClubWebRequest

interface ClubRequestMapper {
    fun createClubWebRequestToDto(webRequest: List<CreateClubWebRequest>): List<CreateClubRequest>
}