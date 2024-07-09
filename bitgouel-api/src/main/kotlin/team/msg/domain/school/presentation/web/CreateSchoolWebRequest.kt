package team.msg.domain.school.presentation.web

import team.msg.common.enums.Line
import team.msg.common.validation.NotBlankStringList
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class CreateSchoolWebRequest(

    @field:NotBlank
    val schoolName: String,

    @field:NotNull
    val line: Line,

    @field:NotBlankStringList
    val departments: List<String>

)