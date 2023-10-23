package team.msg.domain.student.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import team.msg.common.entity.BaseUUIDEntity
import team.msg.common.enum.ApproveStatus
import team.msg.domain.teacher.model.Teacher
import java.time.LocalDateTime
import java.util.*

@Entity
class StudentActivity(

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

) : BaseUUIDEntity(){

    override fun getId(): UUID = id

    fun updateStudentActivity(title: String, content: String, credit: Int, activityDate: LocalDateTime): StudentActivity {
        this.title = title
        this.content = content
        this.credit = credit
        this.approveStatus = ApproveStatus.PENDING
        return this
    }
}