package team.msg.domain.lecture.presentation.data.request

import team.msg.domain.lecture.enums.LectureType

data class QueryAllLectureRequest (
    val lectureType: LectureType?
)