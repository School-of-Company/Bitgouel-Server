package team.msg.domain.admin.repository

import org.springframework.data.repository.CrudRepository
import team.msg.domain.admin.model.Admin
import team.msg.domain.user.model.User
import java.util.UUID

interface AdminRepository : CrudRepository<Admin,UUID> {
    fun findByUser(user: User): Admin?
}