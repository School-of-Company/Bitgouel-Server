package team.msg.domain.student.mapper

import org.springframework.stereotype.Component
import team.msg.domain.student.presentation.data.request.CreateStudentActivityRequest
import team.msg.domain.student.presentation.data.request.UpdateStudentActivityRequest
import team.msg.domain.student.presentation.data.web.CreateStudentActivityWebRequest
import team.msg.domain.student.presentation.data.web.UpdateStudentActivityWebRequest

@Component
class StudentActivityMapperImpl : StudentActivityMapper {

    /**
     * StudentActivity 생성 Web Request를 애플리케이션 영역에서 사용될 Dto 로 매핑합니다.
     */
    override fun createStudentActivityWebRequestToDto(webRequest: CreateStudentActivityWebRequest): CreateStudentActivityRequest =
        CreateStudentActivityRequest(
            title = webRequest.title,
            content = webRequest.content,
            credit = webRequest.credit,
            activityDate = webRequest.activityDate
        )

    /**
     * StudentActivity 생성 Web Request를 애플리케이션 영역에서 사용될 Dto로 매핑합니다.
     */
    override fun updateStudentActivityWebRequestToDto(webRequest: UpdateStudentActivityWebRequest): UpdateStudentActivityRequest =
        UpdateStudentActivityRequest(
            title = webRequest.title,
            content = webRequest.content,
            credit = webRequest.credit,
            activityDate = webRequest.activityDate
        )
}