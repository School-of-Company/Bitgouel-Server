package team.msg.domain.user.handler

import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import team.msg.domain.admin.exception.AdminNotFoundException
import team.msg.domain.admin.repository.AdminRepository
import team.msg.domain.auth.exception.UnApprovedUserException
import team.msg.domain.bbozzak.exception.BbozzakNotFoundException
import team.msg.domain.bbozzak.repository.BbozzakRepository
import team.msg.domain.company.exception.CompanyNotFoundException
import team.msg.domain.company.repository.CompanyInstructorRepository
import team.msg.domain.government.GovernmentNotFoundException
import team.msg.domain.government.repository.GovernmentRepository
import team.msg.domain.lecture.repository.RegisteredLectureRepository
import team.msg.domain.professor.exception.ProfessorNotFoundException
import team.msg.domain.professor.repository.ProfessorRepository
import team.msg.domain.student.exception.StudentNotFoundException
import team.msg.domain.student.repository.StudentActivityRepository
import team.msg.domain.student.repository.StudentRepository
import team.msg.domain.teacher.exception.TeacherNotFoundException
import team.msg.domain.teacher.repository.TeacherRepository
import team.msg.domain.user.enums.Authority.*
import team.msg.domain.user.event.WithdrawUserEvent
import team.msg.domain.user.repository.UserRepository

@Component
class UserEventHandler(
    private val userRepository: UserRepository,
    private val studentRepository: StudentRepository,
    private val studentActivityRepository: StudentActivityRepository,
    private val bbozzakRepository: BbozzakRepository,
    private val teacherRepository: TeacherRepository,
    private val professorRepository: ProfessorRepository,
    private val companyInstructorRepository: CompanyInstructorRepository,
    private val governmentRepository: GovernmentRepository,
    private val registeredLectureRepository: RegisteredLectureRepository,
    private val adminRepository: AdminRepository
) {

    /**
     * User 의 delete 이벤트가 발행되면 User 를 삭제하는 핸들입니다.
     * @param user delete 이벤트
     */
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    fun withdrawUserHandler(event: WithdrawUserEvent) {
        val user = event.user

        when (user.authority) {
            ROLE_STUDENT -> {
                val student = studentRepository.findByUser(user)
                    ?: throw StudentNotFoundException("존재하지 않는 학생 입니다. info : [ userId = ${user.id} ]")
                val studentActivity = studentActivityRepository.findAllByStudent(student)
                val registeredLecture = registeredLectureRepository.findAllByStudent(student)

                studentActivityRepository.deleteAll(studentActivity)
                registeredLectureRepository.deleteAll(registeredLecture)
                studentRepository.delete(student)
            }
            ROLE_ADMIN -> {
                val admin = adminRepository.findByUser(user)
                    ?: throw AdminNotFoundException("존재하지 않는 어드민 입니다. info : [ userId = ${user.id} ]")

                adminRepository.delete(admin)
            }
            ROLE_BBOZZAK -> {
                val bbozzak = bbozzakRepository.findByUser(user)
                    ?: throw BbozzakNotFoundException("존재하지 않는 뽀짝샘 입니다. info : [ userId = ${user.id} ]")

                bbozzakRepository.delete(bbozzak)
            }
            ROLE_TEACHER -> {
                val teacher = teacherRepository.findByUser(user)
                    ?: throw TeacherNotFoundException("존재하지 않는 선생님 입니다. info : [ userId = ${user.id} ]")

                teacherRepository.delete(teacher)
            }
            ROLE_PROFESSOR -> {
                val professor = professorRepository.findByUser(user)
                    ?: throw ProfessorNotFoundException("존재하지 않는 대학 교수 입니다. info : [ userId = ${user.id} ]")

                professorRepository.delete(professor)
            }
            ROLE_COMPANY_INSTRUCTOR -> {
                val companyInstructor = companyInstructorRepository.findByUser(user)
                    ?: throw CompanyNotFoundException("존재하지 않는 기업 강사 입니다. info : [ userId = ${user.id} ]")

                companyInstructorRepository.delete(companyInstructor)
            }
            ROLE_GOVERNMENT -> {
                val government = governmentRepository.findByUser(user)
                    ?: throw GovernmentNotFoundException("존재하지 않는 유관 기관 입니다. info : [ userId = ${user.id} ]")

                governmentRepository.delete(government)
            }

            else -> throw UnApprovedUserException("회원가입 승인 대기 중인 유저입니다. info : [ userId = ${user.id} ]")
        }
    }
}