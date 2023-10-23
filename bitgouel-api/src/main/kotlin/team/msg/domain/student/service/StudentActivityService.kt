package team.msg.domain.student.service

import team.msg.domain.student.presentation.data.request.CreateStudentActivityRequest
import team.msg.domain.student.presentation.data.request.UpdateStudentActivityRequest
import java.util.UUID

interface StudentActivityService {
    fun createStudentActivity(request: CreateStudentActivityRequest)
    fun updateStudentActivity(id: UUID, request: UpdateStudentActivityRequest)
}