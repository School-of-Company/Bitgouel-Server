package domain.teacher.model

import common.entity.BaseUUIDEntity
import domain.school.model.School
import domain.user.model.User
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToOne
import java.util.UUID

@Entity
class Teacher(

    override val id: UUID,

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.MERGE])
    @JoinColumn(name = "user_id", columnDefinition = "BINARY(16)", nullable = false)
    val user: User?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", columnDefinition = "BINARAY(16)", nullable = false)
    val school: School

) : BaseUUIDEntity(id)