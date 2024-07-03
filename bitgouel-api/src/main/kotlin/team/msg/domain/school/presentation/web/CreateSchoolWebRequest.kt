package team.msg.domain.school.presentation.web

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import team.msg.common.enums.Field
import team.msg.common.enums.Line

data class CreateSchoolWebRequest(

    @field:NotBlank
    val schoolName: String,

    @field:NotNull
    val field: Field,

    @field:NotNull
    val line: Line,

    val departments: List<String>

)