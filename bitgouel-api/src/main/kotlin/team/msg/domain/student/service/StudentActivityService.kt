package team.msg.domain.student.service

import org.springframework.data.domain.Pageable
import team.msg.domain.student.presentation.data.request.CreateStudentActivityRequest
import team.msg.domain.student.presentation.data.request.UpdateStudentActivityRequest
import team.msg.domain.student.presentation.data.response.AllStudentActivitiesResponse
import team.msg.domain.student.presentation.data.response.MyStudentActivitiesByStudentResponse
import team.msg.domain.student.presentation.data.response.StudentActivitiesByStudentResponse
import java.util.*

interface StudentActivityService {
    fun createStudentActivity(request: CreateStudentActivityRequest)
    fun updateStudentActivity(id: UUID, request: UpdateStudentActivityRequest)
    fun deleteStudentActivity(id: UUID)
    fun rejectStudentActivity(id: UUID)
    fun approveStudentActivity(id: UUID)
    fun queryAllStudentActivity(pageable: Pageable): AllStudentActivitiesResponse
    fun queryStudentActivitiesByStudent(studentId: UUID, pageable: Pageable): StudentActivitiesByStudentResponse
    fun queryMyStudentActivity(pageable: Pageable): MyStudentActivitiesByStudentResponse
}