package team.msg.domain.lecture.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import team.msg.common.entity.BaseUUIDEntity
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

@Entity
class LectureDate (
    @get:JvmName("getIdentifier")
    override var id: UUID,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id", columnDefinition = "BINARY(16)")
    val lecture: Lecture,

    @Column(nullable = false, updatable = false, columnDefinition = "DATETIME(6)")
    val completeDate: LocalDate,

    @Column(nullable = false, updatable = false, columnDefinition = "DATETIME(6)")
    val startTime: LocalTime,

    @Column(nullable = false, updatable = false, columnDefinition = "DATETIME(6)")
    val endTime: LocalTime,
) : BaseUUIDEntity(id)