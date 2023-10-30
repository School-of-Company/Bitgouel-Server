package team.msg.domain.lecture.service

import team.msg.domain.lecture.presentation.data.request.CreateLectureRequest
import java.util.UUID

interface LectureService {
    fun createLecture(request: CreateLectureRequest)
    fun signUpLecture(id: UUID)
    fun approveLecture(id: UUID)
    fun rejectLecture(id: UUID)
}