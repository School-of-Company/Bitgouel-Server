package team.msg.domain.lecture.presentation.data.web

import javax.persistence.EnumType
import javax.persistence.Enumerated
import org.springframework.web.bind.annotation.RequestParam
import team.msg.domain.lecture.enums.LectureType

data class QueryAllLecturesWebRequest(
    @RequestParam
    @Enumerated(EnumType.STRING)
    val type: LectureType?
)
