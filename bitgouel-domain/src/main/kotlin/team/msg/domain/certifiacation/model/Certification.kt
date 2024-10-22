package team.msg.domain.certifiacation.model

import javax.persistence.Column
import javax.persistence.Entity
import team.msg.common.entity.BaseUUIDEntity
import java.time.LocalDate
import java.util.*

@Entity
class Certification(

    @get:JvmName("getIdentifier")
    override var id: UUID,

    override var ulid: String,

    @Column(name = "student_id", columnDefinition = "BINARY(16)")
    val studentId: UUID,

    @Column(columnDefinition = "VARCHAR(30)", nullable = false)
    val name: String,

    @Column(columnDefinition = "DATE", nullable = false)
    val acquisitionDate: LocalDate

) : BaseUUIDEntity(id, ulid)