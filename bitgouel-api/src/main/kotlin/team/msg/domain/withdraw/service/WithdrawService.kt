package team.msg.domain.withdraw.service

import team.msg.domain.withdraw.presentation.WithdrawStudentResponses

interface WithdrawService {
    fun queryWithdrawStudent(cohort: Int): WithdrawStudentResponses
}