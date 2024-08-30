package team.msg.domain.student.repository

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.repository.CrudRepository
import team.msg.domain.club.model.Club
import team.msg.domain.student.model.Student
import team.msg.domain.user.model.User
import java.util.*

interface StudentRepository : CrudRepository<Student, UUID> {

    @EntityGraph(attributePaths = ["user"], type = EntityGraph.EntityGraphType.FETCH)
    fun findByUser(user: User): Student?
    @EntityGraph(attributePaths = ["user"], type = EntityGraph.EntityGraphType.FETCH)
    fun findStudentById(id: UUID): Student?
    @EntityGraph(attributePaths = ["user"], type = EntityGraph.EntityGraphType.FETCH)
    fun findByIdAndClub(id: UUID, club: Club): Student?
    @EntityGraph(attributePaths = ["user"], type = EntityGraph.EntityGraphType.FETCH)
    fun findAllByClub(club: Club): List<Student>
    @EntityGraph(attributePaths = ["user"], type = EntityGraph.EntityGraphType.FETCH)
    fun findAllByCohort(cohort: Int): List<Student>
    fun existsByClub(club: Club): Boolean
    fun findByIdIn(ids: List<UUID>): List<Student>
    fun countByClub(club: Club): Int
    fun countByClubAndGrade(club: Club, grade: Int): Int
}
