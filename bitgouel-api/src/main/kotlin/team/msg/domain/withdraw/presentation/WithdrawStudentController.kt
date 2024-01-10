package team.msg.domain.withdraw.presentation

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import team.msg.domain.withdraw.presentation.data.response.WithdrawStudentResponses
import team.msg.domain.withdraw.service.WithdrawService

@RestController
@RequestMapping("/withdraw")
class WithdrawStudentController(
    private val withdrawService: WithdrawService
) {

    @GetMapping
    fun queryWithdrawStudent(@RequestParam cohort: Int): ResponseEntity<WithdrawStudentResponses> {
        val response = withdrawService.queryWithdrawStudent(cohort)
        return ResponseEntity.ok(response)
    }
}