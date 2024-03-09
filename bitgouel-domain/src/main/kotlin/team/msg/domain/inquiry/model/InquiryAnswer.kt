package team.msg.domain.inquiry.model

import team.msg.common.entity.BaseUUIDEntity
import team.msg.domain.admin.model.Admin
import java.util.*
import javax.persistence.*

@Entity
class InquiryAnswer(
    @get:JvmName("getIdentifier")
    override var id: UUID,

    @Column(columnDefinition = "VARCHAR(500)", nullable = false)
    val answer: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", columnDefinition = "BINARY(16)", nullable = false)
    val admin: Admin,

    @Column(name = "inquiry_id", columnDefinition = "BINARY(16)", nullable = false)
    val inquiryId: UUID

) : BaseUUIDEntity(id)