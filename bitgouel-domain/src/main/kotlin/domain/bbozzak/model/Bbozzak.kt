package domain.bbozzak.model

import common.entity.BaseUUIDEntity
import domain.club.model.Club
import domain.user.model.User
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToOne
import java.util.UUID

@Entity
class Bbozzak(

    override val id: UUID,

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.MERGE])
    @JoinColumn(name = "user_id", columnDefinition = "BINARY(16)", nullable = false)
    val user: User?,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", columnDefinition = "BINARY(16)", nullable = false)
    val club: Club,

) : BaseUUIDEntity(id)