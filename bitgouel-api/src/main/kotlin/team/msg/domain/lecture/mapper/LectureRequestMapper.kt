package team.msg.domain.lecture.mapper

import team.msg.domain.lecture.presentation.data.request.CreateLectureRequest
import team.msg.domain.lecture.presentation.data.request.QueryAllDepartmentsRequest
import team.msg.domain.lecture.presentation.data.request.QueryAllLectureRequest
import team.msg.domain.lecture.presentation.data.request.QueryAllLinesRequest
import team.msg.domain.lecture.presentation.data.request.QueryAllDivisionsRequest
import team.msg.domain.lecture.presentation.data.request.QueryAllSignedUpStudentsRequest
import team.msg.domain.lecture.presentation.data.request.UpdateLectureRequest
import team.msg.domain.lecture.presentation.data.web.CreateLectureWebRequest
import team.msg.domain.lecture.presentation.data.web.QueryAllDepartmentsWebRequest
import team.msg.domain.lecture.presentation.data.web.QueryAllDivisionsWebRequest
import team.msg.domain.lecture.presentation.data.web.QueryAllLecturesWebRequest
import team.msg.domain.lecture.presentation.data.web.QueryAllLinesWebRequest
import team.msg.domain.lecture.presentation.data.web.QueryAllSignedUpStudentsWebRequest
import team.msg.domain.lecture.presentation.data.web.UpdateLectureWebRequest

interface LectureRequestMapper {
    fun createLectureWebRequestToDto(webRequest: CreateLectureWebRequest): CreateLectureRequest
    fun updateLectureWebRequestToDto(webRequest: UpdateLectureWebRequest): UpdateLectureRequest
    fun queryLectureWebRequestToDto(webRequest: QueryAllLecturesWebRequest): QueryAllLectureRequest
    fun queryAllLinesWebRequestToDto(webRequest: QueryAllLinesWebRequest): QueryAllLinesRequest
    fun queryAllDepartmentsWebRequestToDto(webRequest: QueryAllDepartmentsWebRequest): QueryAllDepartmentsRequest
    fun queryAllDivisionsWebRequestToDto(webRequest: QueryAllDivisionsWebRequest): QueryAllDivisionsRequest
    fun queryAllSignedUpStudentsWebRequestToDto(webRequest: QueryAllSignedUpStudentsWebRequest): QueryAllSignedUpStudentsRequest
}