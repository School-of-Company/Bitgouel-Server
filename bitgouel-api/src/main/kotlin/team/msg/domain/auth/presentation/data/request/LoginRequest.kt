package team.msg.domain.auth.presentation.data.request

data class LoginRequest(
    val email: String,
    val password: String
)