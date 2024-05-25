package team.msg.domain.auth.presentation.data.web

import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import team.msg.domain.school.enums.HighSchool

data class GovernmentSignUpWebRequest(
    @field:Email
    @field:NotNull
    val email: String,

    @field:NotBlank
    val name: String,

    @field:NotBlank
    @field:Pattern(regexp = "^010[0-9]{8}\$")
    val phoneNumber: String,

    val password: String,

    @field:NotNull
    @Enumerated(EnumType.STRING)
    val highSchool: HighSchool,

    @field:NotBlank
    val clubName: String,

    @field:NotBlank
    val governmentName: String,

    @field:NotBlank
    val position: String,

    @field:NotBlank
    val sectors: String
)