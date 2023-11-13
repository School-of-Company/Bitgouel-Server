package team.msg.domain.certifiacation.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import team.msg.common.entity.BaseUUIDEntity
import team.msg.domain.user.model.User
import java.time.LocalDate
import java.util.*

@Entity
class Certification(

    @get:JvmName("getIdentifier")
    override var id: UUID,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", columnDefinition = "BINARY(16)")
    val user: User,

    @Column(columnDefinition = "VARCHAR(30)", nullable = false)
    val name: String,

    @Column(columnDefinition = "DATE", nullable = false)
    val acquisitionDate: LocalDate,

) : BaseUUIDEntity(id)