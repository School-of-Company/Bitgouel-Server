package team.msg.domain.lecture.repository.custom.projection

import com.querydsl.core.annotations.QueryProjection
import team.msg.domain.lecture.model.Lecture

data class LectureListProjection @QueryProjection constructor (
    val lecture: Lecture,
    val registeredLectureCount: Long
)