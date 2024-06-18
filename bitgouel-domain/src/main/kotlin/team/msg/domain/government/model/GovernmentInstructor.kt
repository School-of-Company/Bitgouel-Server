package team.msg.domain.government.model

import team.msg.common.entity.BaseUUIDEntity
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToOne
import team.msg.domain.club.model.Club
import team.msg.domain.user.model.User
import java.util.UUID

@Entity
class GovernmentInstructor(

    @get:JvmName("getIdentifier")
    override var id: UUID,

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.MERGE])
    @JoinColumn(name = "user_id", columnDefinition = "BINARY(16)")
    val user: User?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    val club: Club,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "government_id")
    val government: Government,

    @Column(columnDefinition = "VARCHAR(20)", nullable = false)
    val position: String,

    @Column(columnDefinition = "VARCHAR(20)", nullable = false)
    val sectors: String

) : BaseUUIDEntity(id)