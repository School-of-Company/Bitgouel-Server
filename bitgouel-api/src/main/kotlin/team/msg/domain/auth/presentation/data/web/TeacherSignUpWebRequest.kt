package team.msg.domain.auth.presentation.data.web

import team.msg.domain.school.enums.HighSchool
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

data class TeacherSignUpWebRequest(
    @field:Email
    @field:NotNull
    val email: String,

    @field:NotBlank
    val name: String,

    @field:NotBlank
    @field:Pattern(regexp = "^010[0-9]{8}\$")
    val phoneNumber: String,

    @field:Pattern(regexp = "^(?=.*[A-Za-z0-9])[A-Za-z0-9!@#\\\\\$%^&*]{8,24}\$")
    val password: String,

    @field:NotNull
    @Enumerated(EnumType.STRING)
    val highSchool: HighSchool,

    @field:NotBlank
    val clubName: String
)