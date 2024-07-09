package team.msg.domain.company.repository

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.repository.CrudRepository
import team.msg.domain.club.model.Club
import team.msg.domain.company.model.Company
import team.msg.domain.company.model.CompanyInstructor
import team.msg.domain.user.model.User
import java.util.*

interface CompanyInstructorRepository : CrudRepository<CompanyInstructor, UUID> {

    @EntityGraph(attributePaths = ["user"], type = EntityGraph.EntityGraphType.FETCH)
    fun findByUser(user: User): CompanyInstructor?

    fun existsByClub(club: Club): Boolean

    fun existsByCompany(company: Company): Boolean
}