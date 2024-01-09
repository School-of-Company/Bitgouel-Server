package team.msg.domain.withdraw.service

import org.springframework.stereotype.Service
import team.msg.domain.student.repository.StudentRepository
import team.msg.domain.withdraw.presentation.WithdrawStudentResponse
import team.msg.domain.withdraw.presentation.WithdrawStudentResponses
import team.msg.domain.withdrow.repository.WithdrawStudentRepository

@Service
class WithdrawServiceImpl(
    private val studentRepository: StudentRepository,
    private val withdrawStudentRepository: WithdrawStudentRepository
) : WithdrawService {



    override fun queryWithdrawStudent(cohort: Int): WithdrawStudentResponses {
        val students = studentRepository.findAllByCohort(cohort)

        val withdrawStudents = withdrawStudentRepository.findByStudentIn(students)

        return WithdrawStudentResponse.listOf(withdrawStudents)
    }


}