package team.msg.domain.user.presentation.data.response

import team.msg.common.enums.ApproveStatus
import team.msg.domain.user.enums.Authority
import team.msg.domain.user.model.User
import java.util.*

data class UserResponse (
    val id: UUID,
    val name: String,
    val authority: Authority
) {
    companion object {
        fun listOf(user: User, organization: String) = UserPageResponse(
            id = user.id,
            name = user.name,
            email = user.email,
            phoneNumber = user.phoneNumber.run {
                "${substring(0, 3)}-${substring(3, 7)}-${substring(7)}"
            },
            authority = user.authority,
            organization = organization
        )

        fun of(user: User) = AdminUserResponse(
            id = user.id,
            name = user.name,
            authority = user.authority,
            approveStatus = user.approveStatus,
            phoneNumber = user.phoneNumber,
            email = user.email
        )

        fun listOf(users: List<User>) = UsersResponse(
            users.map {
                of(it)
            }
        )

        fun detailOf(user: User) = UserDetailsResponse(
            id = user.id,
            name = user.name,
            authority = user.authority,
            approveStatus = user.approveStatus
        )
    }
}

data class UserPageResponse (
    val id: UUID,
    val name: String,
    val email: String,
    val phoneNumber: String,
    val authority: Authority,
    val organization: String
)

data class AdminUserResponse(
    val id: UUID,
    val name: String,
    val authority: Authority,
    val approveStatus: ApproveStatus,
    val phoneNumber: String,
    val email: String
)

data class UsersResponse(
    val users: List<AdminUserResponse>
)

data class UserDetailsResponse(
    val id: UUID,
    val name: String,
    val authority: Authority,
    val approveStatus: ApproveStatus
)