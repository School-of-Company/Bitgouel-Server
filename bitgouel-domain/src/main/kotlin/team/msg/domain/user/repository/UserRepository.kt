package team.msg.domain.user.repository

import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.repository.CrudRepository
import team.msg.domain.user.model.User
import team.msg.domain.user.repository.custom.CustomUserRepository
import java.util.UUID

interface UserRepository : CrudRepository<User, UUID>, CustomUserRepository {
    fun existsByEmail(email: String): Boolean
    fun existsByPhoneNumber(phoneNumber: String): Boolean
    fun findByEmail(email: String): User?
    fun findByIdIn(ids: List<UUID>): List<User>
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    fun deleteByIdIn(ids: List<UUID>)
}