package team.msg.domain.inquiry.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import team.msg.common.entity.BaseUUIDEntity
import team.msg.domain.inquiry.enums.AnswerStatus
import java.util.*

@Entity
class Inquiry(
    @get:JvmName("getIdentifier")
    override var id: UUID,

    @Column(name = "user_id", columnDefinition = "BINARY(16)", nullable = false)
    val userId: UUID,

    @Column(columnDefinition = "VARCHAR(1000)", nullable = false)
    val question: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "answer_status", nullable = false)
    val answerStatus: AnswerStatus
) : BaseUUIDEntity(id)