package team.msg.domain.company.model

import javax.persistence.Column
import javax.persistence.Entity
import team.msg.common.entity.BaseUUIDEntity
import java.util.*

@Entity
class Company(

    @get:JvmName("getIdentifier")
    override var id: UUID,

    @Column(name = "name")
    val name: String

) : BaseUUIDEntity(id)