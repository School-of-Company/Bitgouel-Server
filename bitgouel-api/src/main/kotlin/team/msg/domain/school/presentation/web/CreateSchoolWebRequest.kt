package team.msg.domain.school.presentation.web

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import team.msg.common.enums.Field
import team.msg.common.enums.Line
import team.msg.domain.club.presentation.web.request.CreateClubWebRequest

data class CreateSchoolWebRequest(

    @field:NotBlank
    val schoolName: String,

    @field:NotNull
    val schoolField: Field,

    @field:NotNull
    val line: Line,

    val departments: List<String>,

    val club: List<CreateClubWebRequest>

)