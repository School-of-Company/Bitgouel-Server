package team.msg.domain.student.mapper

import org.springframework.stereotype.Component
import team.msg.domain.student.presentation.data.request.CreateStudentActivityRequest
import team.msg.domain.student.presentation.data.web.CreateStudentActivityWebRequest

@Component
class StudentActivityMapperImpl : StudentActivityMapper {

    /**
     * StudentActivity 생성 Web Request 를 애플리케이션 영역에서 사용될 Dto 로 매핑합니다.
     */
    override fun createStudentActivityWebRequestToDto(webRequest: CreateStudentActivityWebRequest): CreateStudentActivityRequest =
        CreateStudentActivityRequest(
            title = webRequest.title,
            content = webRequest.content,
            credit = webRequest.credit,
            activityDate = webRequest.activityDate
        )
}