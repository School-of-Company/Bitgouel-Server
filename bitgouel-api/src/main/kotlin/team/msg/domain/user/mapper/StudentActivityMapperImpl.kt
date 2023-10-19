package team.msg.domain.user.mapper

import team.msg.domain.user.presentation.data.request.CreateStudentActivityRequest
import team.msg.domain.user.presentation.data.web.CreateStudentActivityWebRequest

class StudentActivityMapperImpl : StudentActivityMapper {
    override fun studentActivityWebRequestToDto(webRequest: CreateStudentActivityWebRequest): CreateStudentActivityRequest =
        CreateStudentActivityRequest(
            title = webRequest.title,
            content = webRequest.content,
            credit = webRequest.credit,
            createdAt = webRequest.createdAt
        )
}