package team.msg.domain.student.repository

import org.springframework.boot.autoconfigure.security.SecurityProperties.User

interface CustomStudentActivityHistoryRepository {
    fun deleteAllByUsers(users: List<User>)
}