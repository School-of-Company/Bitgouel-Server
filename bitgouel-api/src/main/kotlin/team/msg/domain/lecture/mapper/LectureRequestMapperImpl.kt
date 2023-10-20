package team.msg.domain.lecture.mapper

import org.springframework.stereotype.Component
import team.msg.domain.lecture.presentation.data.request.CreateLectureRequest
import team.msg.domain.lecture.presentation.data.web.CreateLectureWebRequest

@Component
class LectureRequestMapperImpl : LectureRequestMapper{
    /**
     * Lecture 개설 Web Request 를 애플리케이션 영역에서 사용될 Dto 로 매핑합니다.
     * @param LectureCreateWebRequest
     */
    override fun createLectureWebRequestToDto(request: CreateLectureWebRequest) = CreateLectureRequest(
        request.name,
        request.content,
        request.startDate,
        request.endDate,
        request.completeDate,
        request.lectureType,
        request.credit,
        request.maxRegisteredUser
    )
}