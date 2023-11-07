package team.msg.domain.user.presentation.data.response

import team.msg.domain.user.enums.Authority
import team.msg.domain.user.model.User
import java.util.*

data class UserResponse (
    val id: UUID,
    val name: String,
    val authority: Authority
) {
    companion object {
        fun pageOf(user: User, organization: String) = UserPageResponse(
            name = user.name,
            email = user.email,
            phoneNumber = user.phoneNumber,
            authority = user.authority,
            organization = organization
        )
    }
}

data class UserPageResponse (
    val name: String,
    val email: String,
    val phoneNumber: String,
    val authority: Authority,
    val organization: String
)