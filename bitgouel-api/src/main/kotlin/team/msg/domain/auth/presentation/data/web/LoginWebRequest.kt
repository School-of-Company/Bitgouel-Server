package team.msg.domain.auth.presentation.data.web

import javax.validation.constraints.Email
import javax.validation.constraints.NotNull

data class LoginWebRequest(
    @field:Email
    @field:NotNull
    val email: String,

    val password: String
)