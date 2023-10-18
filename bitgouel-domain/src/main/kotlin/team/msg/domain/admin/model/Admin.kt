package team.msg.domain.admin.model

import team.msg.common.entity.BaseUUIDEntity
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import team.msg.domain.user.model.User
import java.util.*

@Entity
class Admin(

    override val id: UUID,

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.MERGE])
    @JoinColumn(name = "user_id", columnDefinition = "BINARY(16)")
    val user: User?

) : BaseUUIDEntity(id)