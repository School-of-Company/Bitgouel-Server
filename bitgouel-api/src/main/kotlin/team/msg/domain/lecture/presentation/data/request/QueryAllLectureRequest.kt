package team.msg.domain.lecture.presentation.data.request

import team.msg.common.enum.ApproveStatus
import team.msg.domain.lecture.enum.LectureStatus
import team.msg.domain.lecture.enum.LectureType

data class QueryAllLectureRequest (
    val lectureType: LectureType?,
    val lectureStatus: LectureStatus?,
    val approveStatus: ApproveStatus?
)