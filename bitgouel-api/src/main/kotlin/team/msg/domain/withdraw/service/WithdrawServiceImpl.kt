package team.msg.domain.withdraw.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.msg.domain.student.repository.StudentRepository
import team.msg.domain.withdraw.presentation.data.response.WithdrawStudentResponse
import team.msg.domain.withdraw.presentation.data.response.WithdrawStudentResponses
import team.msg.domain.withdraw.repository.WithdrawStudentRepository

@Service
class WithdrawServiceImpl(
    private val studentRepository: StudentRepository,
    private val withdrawStudentRepository: WithdrawStudentRepository
) : WithdrawService {


    /**
     * 탈퇴예정 학생들을 기수로 검색하는 비즈니스 로직
     * @param 학생 기수
     */
    @Transactional(readOnly = true)
    override fun queryWithdrawStudent(cohort: Int): WithdrawStudentResponses {
        val students = studentRepository.findAllByCohort(cohort)

        val withdrawStudents = withdrawStudentRepository.findByStudentIn(students)

        return WithdrawStudentResponse.listOf(withdrawStudents)
    }

}