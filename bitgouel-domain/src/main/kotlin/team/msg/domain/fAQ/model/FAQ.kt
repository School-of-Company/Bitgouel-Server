package team.msg.domain.fAQ.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import team.msg.common.entity.BaseUUIDEntity
import team.msg.domain.admin.model.Admin
import java.util.*

@Entity
class FAQ(

    @get:JvmName("getIdentifier")
    override var id: UUID,

    @Column(nullable = false)
    val title: String,

    @Column(nullable = false)
    val question: String,

    @Column(nullable = false)
    val answer: String,

    @ManyToOne
    @JoinColumn(name = "admin_id", columnDefinition = "BINARY(16)")
    val admin: Admin

) : BaseUUIDEntity(id)