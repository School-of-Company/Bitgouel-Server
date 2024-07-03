package team.msg.domain.student.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToOne
import team.msg.common.entity.BaseUUIDEntity
import team.msg.domain.club.model.Club
import team.msg.domain.student.enums.StudentRole
import team.msg.domain.user.model.User
import java.util.*

@Entity
class Student(

    @get:JvmName(name = "getIdentifier")
    override var id: UUID,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", columnDefinition = "BINARY(16)")
    val user: User?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    val club: Club,

    @Column(columnDefinition = "TINYINT UNSIGNED", nullable = false)
    val grade: Int,

    @Column(name = "class_room", columnDefinition = "TINYINT UNSIGNED", nullable = false)
    val classRoom: Int,

    @Column(columnDefinition = "TINYINT UNSIGNED", nullable = false)
    val number: Int,

    @Column(columnDefinition = "TINYINT UNSIGNED", nullable = false)
    val cohort: Int,

    @Column(columnDefinition = "INT", nullable = false)
    val credit: Int,

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10)", nullable = false)
    val studentRole: StudentRole

) : BaseUUIDEntity(id)