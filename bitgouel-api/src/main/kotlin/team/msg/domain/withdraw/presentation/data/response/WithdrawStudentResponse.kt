package team.msg.domain.withdraw.presentation.data.response

import team.msg.domain.withdraw.model.WithdrawStudent
import java.util.UUID

data class WithdrawStudentResponse(
    val withdrawId: Long,
    val studentId: UUID,
    val studentName: String
) {
    companion object {
        fun of(withdraw: WithdrawStudent) = WithdrawStudentResponse(
            withdrawId = withdraw.id,
            studentId = withdraw.student.id,
            studentName = withdraw.student.user!!.name
        )

        fun listOf(students: List<WithdrawStudent>) = WithdrawStudentResponses(
            students.map { of(it) }
        )
    }
}

data class WithdrawStudentResponses(
    val students: List<WithdrawStudentResponse>
)
