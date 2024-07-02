package team.msg.domain.lecture.mapper

import org.springframework.stereotype.Component
import team.msg.domain.lecture.presentation.data.request.CreateLectureRequest
import team.msg.domain.lecture.presentation.data.request.LectureDateRequest
import team.msg.domain.lecture.presentation.data.request.QueryAllDepartmentsRequest
import team.msg.domain.lecture.presentation.data.request.QueryAllDivisionsRequest
import team.msg.domain.lecture.presentation.data.request.QueryAllLectureRequest
import team.msg.domain.lecture.presentation.data.request.QueryAllLinesRequest
import team.msg.domain.lecture.presentation.data.request.UpdateLectureRequest
import team.msg.domain.lecture.presentation.data.web.CreateLectureWebRequest
import team.msg.domain.lecture.presentation.data.web.LectureDateWebRequest
import team.msg.domain.lecture.presentation.data.web.QueryAllDepartmentsWebRequest
import team.msg.domain.lecture.presentation.data.web.QueryAllDivisionsWebRequest
import team.msg.domain.lecture.presentation.data.web.QueryAllLecturesWebRequest
import team.msg.domain.lecture.presentation.data.web.QueryAllLinesWebRequest
import team.msg.domain.lecture.presentation.data.web.UpdateLectureWebRequest

@Component
class LectureRequestMapperImpl : LectureRequestMapper{
    override fun createLectureWebRequestToDto(webRequest: CreateLectureWebRequest) = CreateLectureRequest(
        name = webRequest.name,
        content = webRequest.content,
        semester = webRequest.semester,
        division = webRequest.division,
        department = webRequest.department,
        line = webRequest.line,
        startDate = webRequest.startDate,
        endDate = webRequest.endDate,
        lectureDates = webRequest.lectureDates.map { lectureDateWebRequestToDto(it) },
        lectureType = webRequest.lectureType,
        credit = webRequest.credit,
        maxRegisteredUser = webRequest.maxRegisteredUser,
        userId = webRequest.userId,
        essentialComplete = webRequest.essentialComplete
    )

    override fun updateLectureWebRequestToDto(webRequest: UpdateLectureWebRequest) = UpdateLectureRequest(
        name = webRequest.name,
        content = webRequest.content,
        semester = webRequest.semester,
        division = webRequest.division,
        department = webRequest.department,
        line = webRequest.line,
        startDate = webRequest.startDate,
        endDate = webRequest.endDate,
        lectureDates = webRequest.lectureDates.map { lectureDateWebRequestToDto(it) },
        lectureType = webRequest.lectureType,
        credit = webRequest.credit,
        maxRegisteredUser = webRequest.maxRegisteredUser,
        userId = webRequest.userId,
        essentialComplete = webRequest.essentialComplete
    )

    private fun lectureDateWebRequestToDto(webRequest: LectureDateWebRequest) = LectureDateRequest(
        completeDate = webRequest.completeDate,
        startTime = webRequest.startTime,
        endTime = webRequest.endTime
    )

    override fun queryLectureWebRequestToDto(webRequest: QueryAllLecturesWebRequest) = QueryAllLectureRequest(
        lectureType = webRequest.type
    )
    
    override fun queryAllLinesWebRequestToDto(webRequest: QueryAllLinesWebRequest) = QueryAllLinesRequest(
        division = webRequest.division,
        keyword = webRequest.keyword
    )

    override fun queryAllDepartmentsWebRequestToDto(webRequest: QueryAllDepartmentsWebRequest) = QueryAllDepartmentsRequest(
        keyword = webRequest.keyword
    )

    override fun queryAllDivisionsWebRequestToDto(webRequest: QueryAllDivisionsWebRequest) = QueryAllDivisionsRequest(
        keyword = webRequest.keyword
    )
}