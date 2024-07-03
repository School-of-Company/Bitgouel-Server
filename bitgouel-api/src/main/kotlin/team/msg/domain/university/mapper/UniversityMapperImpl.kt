package team.msg.domain.university.mapper

import org.springframework.stereotype.Component
import team.msg.domain.university.presentation.data.request.CreateUniversityRequest
import team.msg.domain.university.presentation.data.web.CreateUniversityWebRequest

@Component
class UniversityMapperImpl : UniversityMapper {

    override fun createUniversityWebRequestToDto(webRequest: CreateUniversityWebRequest) : CreateUniversityRequest =
        CreateUniversityRequest(
            id = webRequest.id,
            department = webRequest.department,
            universityName = webRequest.universityName
        )
}