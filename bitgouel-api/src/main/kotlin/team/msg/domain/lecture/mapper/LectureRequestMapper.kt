package team.msg.domain.lecture.mapper

import team.msg.domain.lecture.presentation.data.request.CreateLectureRequest
import team.msg.domain.lecture.presentation.data.request.QueryAllLectureRequest
import team.msg.domain.lecture.presentation.web.CreateLectureWebRequest
import team.msg.domain.lecture.presentation.web.QueryAllLecturesWebRequest

interface LectureRequestMapper {
    fun createLectureWebRequestToDto(webRequest: CreateLectureWebRequest): CreateLectureRequest
    fun queryLectureWebRequestToDto(webRequest: QueryAllLecturesWebRequest): QueryAllLectureRequest
}