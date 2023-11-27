package team.msg.domain.lecture.presentation.web

import javax.persistence.EnumType
import javax.persistence.Enumerated
import org.springframework.web.bind.annotation.RequestParam
import team.msg.common.enums.ApproveStatus
import team.msg.domain.lecture.enums.LectureType

data class QueryAllLecturesWebRequest(
    @RequestParam
    @Enumerated(EnumType.STRING)
    val type: LectureType?,

    @RequestParam
    @Enumerated(EnumType.STRING)
    val status: ApproveStatus?
)
