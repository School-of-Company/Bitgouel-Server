package team.msg.domain.inquiry.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.ManyToOne
import team.msg.common.entity.BaseUUIDEntity
import java.util.*

@Entity
class InquiryAnswer(
    @get:JvmName("getIdentifier")
    override var id: UUID,

    @Column(columnDefinition = "VARCHAR(500)", nullable = false)
    val answer: String,

    @Column(name = "user_id", columnDefinition = "BINARY(16)", nullable = false)
    val adminId: UUID,

    @Column(name = "inquiry_id", columnDefinition = "BINARY(16)", nullable = false)
    val inquiryId: UUID

) : BaseUUIDEntity(id)