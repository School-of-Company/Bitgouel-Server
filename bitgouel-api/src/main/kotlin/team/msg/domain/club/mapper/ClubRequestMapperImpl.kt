package team.msg.domain.club.mapper

import org.springframework.stereotype.Component
import team.msg.domain.club.presentation.data.reqeust.CreateClubRequest
import team.msg.domain.club.presentation.data.reqeust.UpdateClubRequest
import team.msg.domain.club.presentation.web.request.CreateClubWebRequest
import team.msg.domain.club.presentation.web.request.UpdateClubWebRequest

@Component
class ClubRequestMapperImpl : ClubRequestMapper {

    override fun createClubWebRequestToDto(webRequest: List<CreateClubWebRequest>): List<CreateClubRequest> =
        webRequest.map {
            CreateClubRequest(
                clubName = it.clubName,
                field = it.field
            )
        }

    override fun updateClubWebRequestToDto(webRequest: List<UpdateClubWebRequest>): List<UpdateClubRequest> =
        webRequest.map {
            UpdateClubRequest(
                clubName = it.clubName,
                field = it.field
            )
        }

}