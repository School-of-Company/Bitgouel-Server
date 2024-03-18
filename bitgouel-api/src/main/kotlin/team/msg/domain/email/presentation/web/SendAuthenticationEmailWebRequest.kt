package team.msg.domain.email.presentation.web

import javax.validation.constraints.Email
import javax.validation.constraints.NotNull

data class SendAuthenticationEmailWebRequest(
    @field:Email
    @field:NotNull
    val email: String
)
