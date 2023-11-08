package team.msg.domain.club.presentation.data.response

import team.msg.domain.student.model.Student
import team.msg.domain.user.enums.Authority
import java.util.UUID

data class QueryUserByClubIdResponse(
    val id: UUID,
    val name: String,
    val authority: Authority
) {
    companion object {
        fun listOf(students: List<Student>): List<QueryUserByClubIdResponse> = students.map {
            QueryUserByClubIdResponse(
                id = it.user!!.id,
                name = it.user!!.name,
                authority = it.user!!.authority
            )
        }
    }
}

data class QueryAllUsersByClubIdResponse(
    val users: List<QueryUserByClubIdResponse>
)