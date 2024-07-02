package team.msg.domain.company.model

import team.msg.common.entity.BaseUUIDEntity
import team.msg.domain.club.model.Club
import team.msg.domain.user.model.User
import java.util.*
import javax.persistence.*

@Entity
class CompanyInstructor(

    @get:JvmName("getIdentifier")
    override var id: UUID,

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "user_id", columnDefinition = "BINARY(16)")
    val user: User?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    val club: Club,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    val company: Company

) : BaseUUIDEntity(id)