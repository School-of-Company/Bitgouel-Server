package team.msg.domain.auth.presentation.data.web

import javax.validation.constraints.Email
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

data class LoginWebRequest(
    @field:Email
    @field:NotNull
    val email: String,

    @field:Pattern(regexp = "^(?=.*[A-Za-z0-9])[A-Za-z0-9!@#\\\\\$%^&*]{8,24}\$")
    val password: String
)