package domain.school.model

import common.entity.BaseUUIDEntity
import javax.persistence.Entity
import java.util.UUID

@Entity
class School(

    override val id: UUID,

    val name: String,

    val jobClubName: String

) : BaseUUIDEntity(id)