package team.msg.domain.lecture.mapper

import team.msg.domain.lecture.presentation.data.request.CreateLectureRequest
import team.msg.domain.lecture.presentation.data.web.CreateLectureWebRequest

interface LectureRequestMapper {
    fun createLectureWebRequestToDto(request: CreateLectureWebRequest) : CreateLectureRequest
}