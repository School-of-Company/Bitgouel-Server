package team.msg.domain.school.repository

interface CustomSchoolRepository {
    fun existsOne(id: Long): Boolean
}