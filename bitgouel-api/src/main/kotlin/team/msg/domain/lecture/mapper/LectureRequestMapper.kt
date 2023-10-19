package team.msg.domain.lecture.mapper

import team.msg.domain.lecture.presentation.data.request.LectureCreateRequest
import team.msg.domain.lecture.presentation.data.web.LectureCreateWebRequest

interface LectureRequestMapper {
    fun lectureCreateWebRequestToDto(request: LectureCreateWebRequest) : LectureCreateRequest
}