package team.msg.domain.auth.presentation.data.web

import javax.validation.constraints.Email
import javax.validation.constraints.NotNull

data class ChangePasswordWebRequest(
    @field:Email
    @field:NotNull
    val email: String,

    val newPassword: String
)