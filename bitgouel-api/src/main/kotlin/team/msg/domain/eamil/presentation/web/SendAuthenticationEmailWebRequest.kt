package team.msg.domain.eamil.presentation.web

import javax.validation.constraints.Email
import javax.validation.constraints.NotNull

data class SendAuthenticationEmailWebRequest(
    @field:Email
    @field:NotNull
    val email: String
)
