package team.msg.domain.lecture.service

import org.springframework.data.domain.Pageable
import team.msg.domain.lecture.presentation.data.request.CreateLectureRequest
import team.msg.domain.lecture.presentation.data.request.QueryAllLectureRequest
import team.msg.domain.lecture.presentation.data.response.LecturesResponse
import team.msg.domain.lecture.presentation.data.response.LectureDetailsResponse
import java.util.UUID

interface LectureService {
    fun createLecture(request: CreateLectureRequest)
    fun queryAllLectures(pageable: Pageable, queryAllLectureRequest: QueryAllLectureRequest): LecturesResponse
    fun queryLectureDetails(id: UUID): LectureDetailsResponse
    fun signUpLecture(id: UUID)
    fun approveLecture(id: UUID)
    fun rejectLecture(id: UUID)
}