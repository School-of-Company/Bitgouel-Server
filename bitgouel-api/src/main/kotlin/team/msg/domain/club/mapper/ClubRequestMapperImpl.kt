package team.msg.domain.club.mapper

import org.springframework.stereotype.Component
import team.msg.domain.club.presentation.data.reqeust.CreateClubRequest
import team.msg.domain.club.presentation.web.request.CreateClubWebRequest

@Component
class ClubRequestMapperImpl : ClubRequestMapper {

    override fun createClubWebRequestToDto(webRequest: List<CreateClubWebRequest>): List<CreateClubRequest> =
        webRequest.map {
            CreateClubRequest(
                clubName = it.clubName,
                field = it.field
            )
        }


}