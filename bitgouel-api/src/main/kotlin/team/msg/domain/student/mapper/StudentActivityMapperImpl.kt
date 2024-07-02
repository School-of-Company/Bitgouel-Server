package team.msg.domain.student.mapper

import org.springframework.stereotype.Component
import team.msg.domain.student.presentation.data.request.CreateStudentActivityRequest
import team.msg.domain.student.presentation.data.request.UpdateStudentActivityRequest
import team.msg.domain.student.presentation.data.web.CreateStudentActivityWebRequest
import team.msg.domain.student.presentation.data.web.UpdateStudentActivityWebRequest

@Component
class StudentActivityMapperImpl : StudentActivityMapper {
    override fun createStudentActivityWebRequestToDto(webRequest: CreateStudentActivityWebRequest): CreateStudentActivityRequest =
        CreateStudentActivityRequest(
            title = webRequest.title,
            content = webRequest.content,
            credit = webRequest.credit,
            activityDate = webRequest.activityDate
        )

    override fun updateStudentActivityWebRequestToDto(webRequest: UpdateStudentActivityWebRequest): UpdateStudentActivityRequest =
        UpdateStudentActivityRequest(
            title = webRequest.title,
            content = webRequest.content,
            credit = webRequest.credit,
            activityDate = webRequest.activityDate
        )
}