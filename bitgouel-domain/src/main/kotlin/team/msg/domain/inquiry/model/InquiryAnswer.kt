package team.msg.domain.inquiry.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import team.msg.common.entity.BaseUUIDEntity
import team.msg.domain.user.model.User
import java.util.*

@Entity
class InquiryAnswer(
    @get:JvmName("getIdentifier")
    override var id: UUID,

    override var ulid: String,

    @Column(columnDefinition = "VARCHAR(1000)", nullable = false)
    val answer: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", columnDefinition = "BINARY(16)", nullable = false)
    val admin: User,

    @Column(name = "inquiry_id", columnDefinition = "BINARY(16)", nullable = false)
    val inquiryId: UUID

) : BaseUUIDEntity(id, ulid)