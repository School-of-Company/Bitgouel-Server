package bitgouel.team.msg.domain.auth.presentation.data.response

import java.time.LocalDateTime

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String,
    val accessExpiredAt: LocalDateTime,
    val refreshExpiredAt: LocalDateTime
)