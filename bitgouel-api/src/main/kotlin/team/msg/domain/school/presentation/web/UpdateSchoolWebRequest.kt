package team.msg.domain.school.presentation.web

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import team.msg.common.enums.Line
import team.msg.common.validation.NotBlankStringList

data class UpdateSchoolWebRequest(

    @field:NotBlank
    val schoolName: String,

    @field:NotNull
    val line: Line,

    @field:NotBlankStringList
    val departments: List<String>

)