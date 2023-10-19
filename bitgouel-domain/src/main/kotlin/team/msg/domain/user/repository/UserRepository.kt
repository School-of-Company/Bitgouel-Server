package team.msg.domain.user.repository

import org.springframework.data.repository.CrudRepository
import team.msg.domain.user.model.User
import java.util.UUID

interface UserRepository : CrudRepository<User, UUID> {
    fun existsByEmail(email: String): Boolean
    fun existsByPhoneNumber(phoneNumber: String): Boolean
}