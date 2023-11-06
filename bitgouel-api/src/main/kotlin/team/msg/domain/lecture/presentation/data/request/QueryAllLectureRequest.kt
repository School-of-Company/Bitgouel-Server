package team.msg.domain.lecture.presentation.data.request

import team.msg.common.enums.ApproveStatus
import team.msg.domain.lecture.enums.LectureType

data class QueryAllLectureRequest (
    val lectureType: LectureType?,
    val approveStatus: ApproveStatus?
)