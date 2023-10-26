package team.msg.domain.government.model

import team.msg.common.entity.BaseUUIDEntity
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import team.msg.domain.club.model.Club
import team.msg.domain.user.model.User
import java.util.UUID

@Entity
class Government(

    @get:JvmName("getIdentifier")
    override var id: UUID,

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.MERGE])
    @JoinColumn(name = "user_id", columnDefinition = "BINARY(16)")
    val user: User?,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    val club: Club,

    @Column(columnDefinition = "VARCHAR(20)", nullable = false)
    val governmentName: String

) : BaseUUIDEntity(id)