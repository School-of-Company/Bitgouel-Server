package team.msg.domain.lecture.repository.custom.projection

import com.querydsl.core.annotations.QueryProjection
import team.msg.domain.lecture.model.Lecture
import team.msg.domain.student.model.Student

data class LectureListProjection @QueryProjection constructor (
    val lecture: Lecture,
    val registeredLectureCount: Long
)

data class SignedUpStudentProjection @QueryProjection constructor(
    val student: Student,
    val isComplete: Boolean
)

data class LectureAndIsCompleteProjection @QueryProjection constructor(
    val lecture: Lecture,
    val isComplete: Boolean
)