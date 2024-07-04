package team.msg.domain.government.repository

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.repository.CrudRepository
import team.msg.domain.club.model.Club
import team.msg.domain.government.model.Government
import team.msg.domain.government.model.GovernmentInstructor
import team.msg.domain.user.model.User
import java.util.*

interface GovernmentInstructorRepository : CrudRepository<GovernmentInstructor, UUID> {

    @EntityGraph(attributePaths = ["user"], type = EntityGraph.EntityGraphType.FETCH)
    fun findByUser(user: User): GovernmentInstructor?

    fun existsByClub(club: Club): Boolean

    fun existsByGovernment(government: Government): Boolean

}