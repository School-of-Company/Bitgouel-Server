package team.msg.domain.university.presentation.data.web

import javax.validation.constraints.NotBlank

data class CreateDepartmentWebRequest(
    @field:NotBlank
    val department: String
)