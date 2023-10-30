package team.msg.domain.teacher.repository

import org.springframework.data.repository.CrudRepository
import team.msg.domain.club.model.Club
import team.msg.domain.teacher.model.Teacher
import team.msg.domain.user.model.User
import java.util.UUID

interface TeacherRepository : CrudRepository<Teacher, UUID> {
    fun findByClub(club: Club): Teacher?
    fun findByUser(user: User): Teacher?
    fun existsByClubAndUser(club: Club, user: User): Boolean
}