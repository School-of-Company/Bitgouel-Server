package team.msg.domain.user.presentation.data.request

data class ModifyPasswordRequest(
    val currentPassword: String,
    val newPassword: String
)
