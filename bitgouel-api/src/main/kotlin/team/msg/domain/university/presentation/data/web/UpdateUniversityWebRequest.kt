package team.msg.domain.university.presentation.data.web

import javax.validation.constraints.NotBlank

data class UpdateUniversityWebRequest(
    @field:NotBlank
    val universityName: String
)