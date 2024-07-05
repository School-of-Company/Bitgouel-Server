package team.msg.domain.lecture.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import java.util.UUID

@Entity
class LectureLocation(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long = 0,

    @Column(name = "lecture_id", columnDefinition = "BINARY(16)")
    val lectureId: UUID,

    @Column(name = "x", columnDefinition = "VARCHAR(30)", nullable = false)
    val x: String,

    @Column(name = "y", columnDefinition = "VARCHAR(30)", nullable = false)
    val y: String,

    @Column(name = "address", nullable = false)
    val address: String,

    @Column(name = "details", nullable = false)
    val details: String
)