package team.msg.domain.company.model

import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToOne
import team.msg.common.entity.BaseUUIDEntity
import team.msg.domain.club.model.Club
import team.msg.domain.user.model.User
import java.util.*

@Entity
class CompanyInstructor(

    @get:JvmName("getIdentifier")
    override var id: UUID,

    override var ulid: String,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", columnDefinition = "BINARY(16)")
    val user: User?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    val club: Club,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    val company: Company

) : BaseUUIDEntity(id, ulid)