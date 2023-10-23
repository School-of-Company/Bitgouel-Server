package team.msg.domain.professor.model

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
class Professor(

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.MERGE])
    @JoinColumn(name = "user_id", columnDefinition = "BINARY(16)")
    val user: User?,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    val club: Club,

    @Column(columnDefinition = "VARCHAR(20)", nullable = false)
    val university: String

) : BaseUUIDEntity() {
    override fun getId(): UUID = id
}