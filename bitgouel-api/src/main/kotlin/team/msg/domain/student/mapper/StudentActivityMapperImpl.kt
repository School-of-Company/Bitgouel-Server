package team.msg.domain.student.mapper

import team.msg.domain.student.presentation.data.request.CreateStudentActivityRequest
import team.msg.domain.student.presentation.data.web.CreateStudentActivityWebRequest

class StudentActivityMapperImpl : StudentActivityMapper {
    override fun studentActivityWebRequestToDto(webRequest: CreateStudentActivityWebRequest): CreateStudentActivityRequest =
        CreateStudentActivityRequest(
            title = webRequest.title,
            content = webRequest.content,
            credit = webRequest.credit,
            createdAt = webRequest.createdAt
        )
}