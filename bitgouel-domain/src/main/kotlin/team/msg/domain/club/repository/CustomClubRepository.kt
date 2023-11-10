package team.msg.domain.club.repository

interface CustomClubRepository {
    fun existsOne(id: Long): Boolean
}