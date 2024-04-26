package team.msg.domain.withdraw.presentation.data.response

import team.msg.domain.withdraw.model.WithdrawStudent
import java.util.UUID

data class WithdrawStudentResponse(
    val withdrawId: Long,
    val userId: UUID,
    val name: String,
    val email: String,
    val phoneNumber: String
) {
    companion object {
        fun of(withdraw: WithdrawStudent) = WithdrawStudentResponse(
            withdrawId = withdraw.id,
            userId = withdraw.student.user!!.id,
            name = withdraw.student.user!!.name,
            email = withdraw.student.user!!.email,
            phoneNumber = withdraw.student.user!!.phoneNumber
        )

        fun listOf(students: List<WithdrawStudent>) = WithdrawStudentResponses(
            students.map { of(it) }
        )
    }
}

data class WithdrawStudentResponses(
    val students: List<WithdrawStudentResponse>
)
