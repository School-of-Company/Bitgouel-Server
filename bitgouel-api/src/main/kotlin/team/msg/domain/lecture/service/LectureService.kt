package team.msg.domain.lecture.service

import team.msg.domain.lecture.enum.LectureStatus
import team.msg.domain.lecture.presentation.data.request.CreateLectureRequest
import team.msg.domain.lecture.presentation.data.response.AllLecturesResponse
import team.msg.domain.lecture.presentation.data.response.LectureDetailsResponse
import java.util.UUID

interface LectureService {
    fun createLecture(request: CreateLectureRequest)
    fun queryAllLectures(status: LectureStatus): AllLecturesResponse
    fun queryLectureDetails(id: UUID): LectureDetailsResponse
    fun signUpLecture(id: UUID)
    fun approveLecture(id: UUID)
    fun rejectLecture(id: UUID)
}