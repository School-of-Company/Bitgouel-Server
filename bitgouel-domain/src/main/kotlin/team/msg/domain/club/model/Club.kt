package team.msg.domain.club.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import team.msg.common.entity.BaseUUIDEntity
import team.msg.domain.school.model.School
import java.util.UUID

@Entity
class Club(

    override val id: UUID,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", nullable = false)
    val school: School,

    @Column(columnDefinition = "VARCHAR(30)", nullable = false)
    val name: String

) : BaseUUIDEntity(id)