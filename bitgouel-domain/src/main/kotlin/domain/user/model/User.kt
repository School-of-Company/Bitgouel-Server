package domain.user.model

import base.BaseUUIDEntity
import javax.persistence.Column
import javax.persistence.Entity
import java.util.*

@Entity
class User(

    override val id: UUID,

    @Column(columnDefinition = "VARCHAR(100)", nullable = false, unique = true)
    val email: String,


) : BaseUUIDEntity(id)