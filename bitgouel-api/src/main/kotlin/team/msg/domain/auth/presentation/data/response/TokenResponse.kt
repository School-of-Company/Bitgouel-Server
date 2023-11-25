package team.msg.domain.auth.presentation.data.response

import team.msg.domain.user.enums.Authority
import java.time.LocalDateTime

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String,
    val accessExpiredAt: LocalDateTime,
    val refreshExpiredAt: LocalDateTime,
    val authority: Authority
)