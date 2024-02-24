package team.msg.domain.student.service

import org.springframework.data.domain.Pageable
import team.msg.domain.student.presentation.data.request.CreateStudentActivityRequest
import team.msg.domain.student.presentation.data.request.UpdateStudentActivityRequest
import team.msg.domain.student.presentation.data.response.StudentActivitiesResponse
import team.msg.domain.student.presentation.data.response.StudentActivityDetailsResponse
import java.util.*

interface StudentActivityService {
    fun createStudentActivity(request: CreateStudentActivityRequest)
    fun updateStudentActivity(id: UUID, request: UpdateStudentActivityRequest)
    fun deleteStudentActivity(id: UUID)
    fun queryAllStudentActivities(pageable: Pageable): StudentActivitiesResponse
    fun queryStudentActivitiesByStudent(studentId: UUID, pageable: Pageable): StudentActivitiesResponse
    fun queryMyStudentActivities(pageable: Pageable): StudentActivitiesResponse
    fun queryStudentActivityDetail(id: UUID): StudentActivityDetailsResponse
}