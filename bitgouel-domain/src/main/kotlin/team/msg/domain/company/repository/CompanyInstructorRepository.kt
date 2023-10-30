package team.msg.domain.company.repository

import org.springframework.data.repository.CrudRepository
import team.msg.domain.club.model.Club
import team.msg.domain.company.model.CompanyInstructor
import team.msg.domain.user.model.User
import java.util.UUID

interface CompanyInstructorRepository : CrudRepository<CompanyInstructor, UUID> {
    fun findByUser(user: User): CompanyInstructor?
    fun existsByClubAndUser(club: Club,user: User): Boolean
}