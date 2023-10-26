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

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.MERGE])
    @JoinColumn(name = "user_id", columnDefinition = "BINARY(16)")
    val user: User?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    val club: Club,

    @Column(columnDefinition = "VARCHAR(20)", nullable = false)
    val company: String

) : BaseUUIDEntity(id)