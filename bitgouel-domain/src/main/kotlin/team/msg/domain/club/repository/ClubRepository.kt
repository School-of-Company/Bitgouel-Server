package team.msg.domain.club.repository

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import team.msg.domain.club.model.Club
import team.msg.domain.school.model.School

interface ClubRepository : JpaRepository<Club, Long>, CustomClubRepository {
    @EntityGraph(attributePaths = ["school"], type = EntityGraph.EntityGraphType.FETCH)
    fun findByNameAndSchool(name: String, school: School): Club?

    @Query("SELECT c FROM Club c JOIN FETCH c.school s WHERE s = :school")
    fun findAllBySchool(school: School): List<Club>
}