package team.msg.domain.school.presentation.web

import team.msg.common.enums.Field
import team.msg.common.enums.Line
import javax.validation.constraints.NotBlank

class UpdateSchoolWebRequest(

    @field:NotBlank
    val schoolName: String,

    val field: Field,

    val line: Line

)