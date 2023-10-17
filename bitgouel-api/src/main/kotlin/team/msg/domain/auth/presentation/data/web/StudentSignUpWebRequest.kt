package team.msg.domain.auth.presentation.data.web

import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.validation.constraints.Email
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import team.msg.domain.school.enums.HighSchool

data class StudentSignUpWebRequest(

    @field:Email
    @field:NotNull
    val email: String,

    @field:NotBlank
    val name: String,

    @field:NotBlank
    val phoneNumber: String,

    @field:Pattern(regexp = "^(?=.*[A-Za-z0-9])[A-Za-z0-9!@#\\\\\$%^&*]{8,24}\$")
    val password: String,

    @field:NotNull
    @Enumerated(EnumType.STRING)
    val highSchool: HighSchool,

    @field:Min(1)
    @field:Max(3)
    val grade: Int,

    @field:NotNull
    val classRoom: Int,

    @field:NotNull
    val number: Int,

    @field:NotNull
    val admissionNumber: Int

)