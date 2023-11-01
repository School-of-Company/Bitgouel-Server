package team.msg.domain.government.repository

import org.springframework.data.repository.CrudRepository
import team.msg.domain.government.model.Government
import team.msg.domain.user.model.User
import java.util.*

interface GovernmentRepository : CrudRepository<Government, UUID> {
    fun findByUser(user: User): Government?
}