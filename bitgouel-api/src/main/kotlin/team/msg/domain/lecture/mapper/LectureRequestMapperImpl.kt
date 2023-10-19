package team.msg.domain.lecture.mapper

import org.springframework.stereotype.Component
import team.msg.domain.lecture.presentation.data.request.LectureCreateRequest
import team.msg.domain.lecture.presentation.data.web.LectureCreateWebRequest

@Component
class LectureRequestMapperImpl : LectureRequestMapper{
    override fun lectureCreateWebRequestToDto(request: LectureCreateWebRequest) = LectureCreateRequest(
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