package team.msg.domain.inquiry.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import team.msg.common.entity.BaseUUIDEntity
import team.msg.domain.inquiry.enums.AnswerStatus
import team.msg.domain.user.model.User
import java.util.*

@Entity
class Inquiry(
    @get:JvmName("getIdentifier")
    override var id: UUID,

    @ManyToOne
    @JoinColumn(name = "user_id", columnDefinition = "BINARY(16)")
    val user: User,

    @Column(columnDefinition = "VARCHAR(100)", nullable = false)
    val question: String,

    @Column(columnDefinition = "VARCHAR(1000)", nullable = false)
    val questionDetail: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "answer_status", nullable = false)
    val answerStatus: AnswerStatus
) : BaseUUIDEntity(id)