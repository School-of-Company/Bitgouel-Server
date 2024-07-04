package team.msg.domain.teacher.repository

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.repository.CrudRepository
import team.msg.domain.club.model.Club
import team.msg.domain.teacher.model.Teacher
import team.msg.domain.user.model.User
import java.util.*

interface TeacherRepository : CrudRepository<Teacher, UUID> {
    @EntityGraph(attributePaths = ["user", "club"])
    fun findByClub(club: Club): Teacher?
    @EntityGraph(attributePaths = ["user", "club"], type = EntityGraph.EntityGraphType.FETCH)
    fun findByUser(user: User): Teacher?
    fun existsByClub(club: Club): Boolean
}