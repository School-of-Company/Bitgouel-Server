package team.msg.domain.lecture.mapper

import org.springframework.stereotype.Component
import team.msg.domain.lecture.presentation.data.request.CreateLectureRequest
import team.msg.domain.lecture.presentation.data.web.CreateLectureWebRequest

@Component
class LectureRequestMapperImpl : LectureRequestMapper{
    /**
     * Lecture 개설 Web Request 를 애플리케이션 영역에서 사용될 Dto 로 매핑합니다.
     * @param CreateLectureWebRequest
     */
    override fun createLectureWebRequestToDto(request: CreateLectureWebRequest) = CreateLectureRequest(
        name = request.name,
        content = request.content,
        startDate = request.startDate,
        endDate = request.endDate,
        completeDate = request.completeDate,
        lectureType = request.lectureType,
        credit = request.credit,
        maxRegisteredUser = request.maxRegisteredUser
    )
}