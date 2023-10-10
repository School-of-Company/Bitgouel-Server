package domain.admin.model

import common.entity.BaseUUIDEntity
import domain.user.model.User
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import java.util.*

@Entity
class Admin(

    override val id: UUID,

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.MERGE])
    @JoinColumn(name = "user_id", columnDefinition = "BINARY(16)")
    val user: User?,

) : BaseUUIDEntity(id)