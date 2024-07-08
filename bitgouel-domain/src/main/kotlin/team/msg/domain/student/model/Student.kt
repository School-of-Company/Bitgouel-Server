package team.msg.domain.student.model

import team.msg.common.entity.BaseUUIDEntity
import team.msg.domain.club.model.Club
import team.msg.domain.student.enums.StudentRole
import team.msg.domain.user.model.User
import java.util.*
import javax.persistence.*

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

    @Column(name = "grade", columnDefinition = "TINYINT UNSIGNED", nullable = false)
    val grade: Int,

    @Column(name = "class_room", columnDefinition = "TINYINT UNSIGNED", nullable = false)
    val classRoom: Int,

    @Column(name = "number", columnDefinition = "TINYINT UNSIGNED", nullable = false)
    val number: Int,

    @Column(name = "cohort", columnDefinition = "TINYINT UNSIGNED", nullable = false)
    val cohort: Int,

    @Column(name = "credit", columnDefinition = "INT", nullable = false)
    val credit: Int,

    @Column(name = "subscription_grade", columnDefinition = "INT", nullable = false)
    val subscriptionGrade: Int,

    @Enumerated(EnumType.STRING)
    @Column(name = "student_role", columnDefinition = "VARCHAR(10)", nullable = false)
    val studentRole: StudentRole

) : BaseUUIDEntity(id)