package team.msg.domain.club.repository.custom

interface CustomClubRepository {
    fun existsOne(id: Long): Boolean
    fun deleteAllBySchoolId(schoolId: Long)
    fun findAllBySchoolName(schoolName: String?): List<String>
}