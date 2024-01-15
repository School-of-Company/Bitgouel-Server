package team.msg.domain.user.presentation.web

import javax.validation.constraints.Pattern

data class ModifyPasswordWebRequest(
    @field:Pattern(regexp = "^(?=.*[A-Za-z0-9])[A-Za-z0-9!@#\\\\\$%^&*]{8,24}\$")
    val currentPassword: String,

    @field:Pattern(regexp = "^(?=.*[A-Za-z0-9])[A-Za-z0-9!@#\\\\\$%^&*]{8,24}\$")
    val newPassword: String
)
