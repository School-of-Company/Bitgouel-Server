package team.msg.domain.lecture.mapper

import org.springframework.stereotype.Component
import team.msg.domain.lecture.presentation.data.request.CreateLectureRequest
import team.msg.domain.lecture.presentation.web.CreateLectureWebRequest

@Component
class LectureRequestMapperImpl : LectureRequestMapper{
    /**
     * Lecture 개설 Web Request 를 애플리케이션 영역에서 사용될 Dto 로 매핑합니다.
     */
    override fun createLectureWebRequestToDto(webRequest: CreateLectureWebRequest) = CreateLectureRequest(
        name = webRequest.name,
        content = webRequest.content,
        startDate = webRequest.startDate,
        endDate = webRequest.endDate,
        completeDate = webRequest.completeDate,
        lectureType = webRequest.lectureType,
        credit = webRequest.credit,
        maxRegisteredUser = webRequest.maxRegisteredUser
    )
}