package team.msg.domain.student.repository

import java.util.UUID

interface CustomStudentRepository {
    fun existOne(id: UUID): Boolean
}