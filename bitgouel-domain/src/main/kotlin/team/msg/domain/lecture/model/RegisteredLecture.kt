package team.msg.domain.lecture.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import team.msg.common.entity.BaseUUIDEntity
import team.msg.domain.lecture.enums.CompleteStatus
import team.msg.domain.student.model.Student
import java.util.UUID

@Entity
class RegisteredLecture(

    @get:JvmName("getIdentifier")
    override var id: UUID,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", columnDefinition = "BINARY(16)")
    val student: Student,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id", columnDefinition = "BINARY(16)")
    val lecture: Lecture,

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(30)", nullable = false)
    val completeStatus: CompleteStatus = CompleteStatus.NOT_COMPLETED_YET
) : BaseUUIDEntity(id) {

    fun isComplete() = completeStatus != CompleteStatus.NOT_COMPLETED_YET
}