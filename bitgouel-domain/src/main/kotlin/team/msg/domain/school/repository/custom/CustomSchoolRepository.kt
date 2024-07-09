package team.msg.domain.school.repository.custom

interface CustomSchoolRepository {
    fun existsOne(id: Long): Boolean
}