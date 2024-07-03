package team.msg.domain.school.presentation.web

import team.msg.common.enums.Field
import team.msg.common.enums.Line
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class UpdateSchoolWebRequest(

    @field:NotBlank
    val schoolName: String,

    @field:NotNull
    val field: Field,

    @field:NotNull
    val line: Line,

    val departments: List<String>

)