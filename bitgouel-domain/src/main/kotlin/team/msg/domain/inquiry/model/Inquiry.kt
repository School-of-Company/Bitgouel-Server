package team.msg.domain.inquiry.model

import javax.persistence.Column
import javax.persistence.Entity
import team.msg.common.entity.BaseUUIDEntity
import java.util.*

@Entity
class Inquiry(
    @get:JvmName("getIdentifier")
    override var id: UUID,

    @Column(name = "user_id", columnDefinition = "BINARY(16)", nullable = false)
    val userId: UUID,

    @Column(columnDefinition = "VARCHAR(1000)", nullable = false)
    val question: String,
) : BaseUUIDEntity(id)