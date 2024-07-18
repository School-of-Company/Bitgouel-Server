package team.msg.domain.club.mapper

import org.springframework.stereotype.Component
import team.msg.domain.club.presentation.data.reqeust.CreateClubRequest
import team.msg.domain.club.presentation.data.reqeust.UpdateClubRequest
import team.msg.domain.club.presentation.web.request.CreateClubWebRequest
import team.msg.domain.club.presentation.web.request.UpdateClubWebRequest

@Component
class ClubRequestMapperImpl : ClubRequestMapper {

    override fun createClubWebRequestToDto(webRequest: CreateClubWebRequest): CreateClubRequest =
        CreateClubRequest(
            clubName = webRequest.name,
            field = webRequest.field
        )

    override fun updateClubWebRequestToDto(webRequest: UpdateClubWebRequest): UpdateClubRequest =
        UpdateClubRequest(
            clubName = webRequest.name,
            field = webRequest.field
        )

}