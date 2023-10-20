package team.msg.domain.student.service

import team.msg.domain.student.presentation.data.request.CreateStudentActivityRequest

interface StudentActivityService {
    fun createStudentActivity(request: CreateStudentActivityRequest)
}