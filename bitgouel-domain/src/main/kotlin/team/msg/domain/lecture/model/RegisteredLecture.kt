package team.msg.domain.lecture.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import team.msg.common.entity.BaseUUIDEntity
import team.msg.domain.student.model.Student
import java.time.LocalDateTime
import java.util.UUID

@Entity
class RegisteredLecture(
    override val id: UUID,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", columnDefinition = "BINARY(16)")
    val student: Student,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", columnDefinition = "BINARY(16)")
    val lecture: Lecture,

    @Column(nullable = false, updatable = false, columnDefinition = "DATETIME(6)")
    val completeDate: LocalDateTime
) : BaseUUIDEntity(id)