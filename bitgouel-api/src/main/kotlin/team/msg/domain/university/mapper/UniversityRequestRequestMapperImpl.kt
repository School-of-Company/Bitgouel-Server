package team.msg.domain.university.mapper

import org.springframework.stereotype.Component
import team.msg.domain.university.presentation.data.request.CreateUniversityRequest
import team.msg.domain.university.presentation.data.web.CreateUniversityWebRequest

@Component
class UniversityRequestRequestMapperImpl : UniversityRequestMapper {

    override fun createUniversityWebRequestToDto(webRequest: CreateUniversityWebRequest) : CreateUniversityRequest =
        CreateUniversityRequest(
            universityName = webRequest.universityName
        )
}