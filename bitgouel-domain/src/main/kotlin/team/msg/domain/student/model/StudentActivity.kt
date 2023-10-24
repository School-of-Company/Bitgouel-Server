package team.msg.domain.student.model

import team.msg.common.entity.BaseUUIDEntity
import team.msg.common.enum.ApproveStatus
import team.msg.domain.teacher.model.Teacher
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
class StudentActivity(

    @get:JvmName("getIdentifier")
    override var id: UUID,

    @Column(columnDefinition = "VARCHAR(100)", nullable = false)
    var title: String,

    @Column(columnDefinition = "VARCHAR(1000)", nullable = false)
    var content: String,

    @Column(columnDefinition = "INT", nullable = false)
    var credit: Int,

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10)", nullable = false)
    var approveStatus: ApproveStatus,

    @Column(nullable = false, columnDefinition = "DATETIME(6)")
    var activityDate: LocalDateTime,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", columnDefinition = "BINARY(16)", nullable = false)
    val student: Student,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", columnDefinition = "BINARY(16)", nullable = false)
    val teacher: Teacher

) : BaseUUIDEntity(id)