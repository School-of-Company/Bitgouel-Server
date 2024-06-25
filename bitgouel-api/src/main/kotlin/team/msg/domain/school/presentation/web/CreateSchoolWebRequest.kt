package team.msg.domain.school.presentation.web

import javax.validation.constraints.NotBlank

class CreateSchoolWebRequest(

    @field:NotBlank
    val schoolName: String

)