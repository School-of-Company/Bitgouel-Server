package team.msg.domain.auth.presentation.data.request

data class ChangePasswordRequest(
    val email: String,
    val newPassword: String
)