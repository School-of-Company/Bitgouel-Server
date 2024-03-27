package team.msg.domain.lecture.model

import team.msg.common.entity.BaseUUIDEntity
import java.util.*
import javax.persistence.*

@Entity
class RegisteredLectureCount(
    @get:JvmName("getIdentifier")
    override var id: UUID,

    @Column(columnDefinition = "TINYINT UNSIGNED", nullable = false)
    val registeredUser: Int,

    @Column(columnDefinition = "TINYINT UNSIGNED", nullable = false)
    val maxRegisteredUser: Int,

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.MERGE])
    val lecture: Lecture
) : BaseUUIDEntity(id)