package team.msg.domain.withdraw.service

import team.msg.domain.withdraw.presentation.data.response.WithdrawStudentResponses

interface WithdrawService {
    fun queryWithdrawStudent(cohort: Int): WithdrawStudentResponses
}