package team.msg.domain.professor.repository

import org.springframework.data.repository.CrudRepository
import team.msg.domain.club.model.Club
import team.msg.domain.professor.model.Professor
import team.msg.domain.user.model.User
import java.util.UUID

interface ProfessorRepository : CrudRepository<Professor, UUID> {
    fun findByUser(user: User): Professor?
    fun existsByClubAndUser(club: Club,user: User): Boolean
}