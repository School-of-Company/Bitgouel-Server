package team.msg.domain.lecture.presentation.data.web

import javax.validation.constraints.NotNull
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import team.msg.domain.lecture.enums.Division

data class QueryAllLinesWebRequest (
    @NotNull
    @RequestBody
    val division: Division,

    @RequestParam(required = false)
    val keyword: String? = null
)