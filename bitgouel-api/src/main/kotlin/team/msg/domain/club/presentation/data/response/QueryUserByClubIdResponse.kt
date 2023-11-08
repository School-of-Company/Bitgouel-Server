package team.msg.domain.club.presentation.data.response

import team.msg.domain.student.model.Student
import team.msg.domain.user.enums.Authority
import team.msg.domain.user.model.User
import java.util.UUID

data class QueryUserByClubIdResponse(
    val id: UUID,
    val name: String,
    val authority: Authority
) {
    companion object {
        fun listOf(users: List<Student>): List<QueryUserByClubIdResponse> = users.map {
            QueryUserByClubIdResponse(
                id = it.id,
                name = it.name,
                authority = it.authority
            )
        }
    }
}

data class QueryAllUsersByClubIdResponse(
    val users: List<QueryUserByClubIdResponse>
)