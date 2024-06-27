package team.msg.domain.user.handler

import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import team.msg.domain.admin.exception.AdminNotFoundException
import team.msg.domain.admin.model.Admin
import team.msg.domain.admin.repository.AdminRepository
import team.msg.domain.auth.exception.UnApprovedUserException
import team.msg.domain.bbozzak.exception.BbozzakNotFoundException
import team.msg.domain.bbozzak.model.Bbozzak
import team.msg.domain.bbozzak.repository.BbozzakRepository
import team.msg.domain.certifiacation.repository.CertificationRepository
import team.msg.domain.company.exception.CompanyNotFoundException
import team.msg.domain.company.model.CompanyInstructor
import team.msg.domain.company.repository.CompanyInstructorRepository
import team.msg.domain.government.GovernmentNotFoundException
import team.msg.domain.government.model.Government
import team.msg.domain.government.repository.GovernmentRepository
import team.msg.domain.inquiry.repository.InquiryAnswerRepository
import team.msg.domain.inquiry.repository.InquiryRepository
import team.msg.domain.lecture.repository.LectureRepository
import team.msg.domain.lecture.repository.RegisteredLectureRepository
import team.msg.domain.post.repository.PostRepository
import team.msg.domain.professor.exception.ProfessorNotFoundException
import team.msg.domain.professor.model.Professor
import team.msg.domain.professor.repository.ProfessorRepository
import team.msg.domain.student.exception.StudentNotFoundException
import team.msg.domain.student.model.Student
import team.msg.domain.student.repository.StudentActivityRepository
import team.msg.domain.student.repository.StudentRepository
import team.msg.domain.teacher.exception.TeacherNotFoundException
import team.msg.domain.teacher.model.Teacher
import team.msg.domain.teacher.repository.TeacherRepository
import team.msg.domain.user.enums.Authority.*
import team.msg.domain.user.event.WithdrawUserEvent
import team.msg.domain.user.model.User
import team.msg.domain.user.repository.UserRepository
import team.msg.domain.withdraw.repository.WithdrawStudentRepository
import java.util.UUID

@Component
class UserEventHandler(
    private val studentRepository: StudentRepository,
    private val studentActivityRepository: StudentActivityRepository,
    private val bbozzakRepository: BbozzakRepository,
    private val teacherRepository: TeacherRepository,
    private val professorRepository: ProfessorRepository,
    private val companyInstructorRepository: CompanyInstructorRepository,
    private val governmentRepository: GovernmentRepository,
    private val registeredLectureRepository: RegisteredLectureRepository,
    private val adminRepository: AdminRepository,
    private val lectureRepository: LectureRepository,
    private val certificationRepository: CertificationRepository,
    private val postRepository: PostRepository,
    private val withdrawStudentRepository: WithdrawStudentRepository,
    private val inquiryRepository: InquiryRepository,
    private val inquiryAnswerRepository: InquiryAnswerRepository,
    private val userRepository: UserRepository
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
                val student = studentRepository findByUser user

                inquiryRepository.deleteAllByUserId(user.id)
                studentActivityRepository.deleteAllByStudentId(student.id)
                registeredLectureRepository.deleteAllByStudentId(student.id)
                certificationRepository.deleteAllByStudentId(student.id)
                withdrawStudentRepository.deleteByStudent(student)
                studentRepository.delete(student)
            }
            ROLE_ADMIN -> {
                val admin = adminRepository findByUser user

                inquiryAnswerRepository.deleteAllByAdminId(admin.id)
                postRepository.deleteAllByUserId(user.id)
                adminRepository.delete(admin)
            }
            ROLE_BBOZZAK -> {
                val bbozzak = bbozzakRepository findByUser user

                inquiryRepository.deleteAllByUserId(user.id)
                postRepository.deleteAllByUserId(user.id)
                bbozzakRepository.delete(bbozzak)
            }
            ROLE_TEACHER -> {
                val teacher = teacherRepository findByUser user

                inquiryRepository.deleteAllByUserId(user.id)
                teacherRepository.delete(teacher)
            }
            ROLE_PROFESSOR -> {
                val professor = professorRepository findByUser user

                inquiryRepository.deleteAllByUserId(user.id)
                lectureRepository.updateAllByUser(user)
                lectureRepository.deleteAllByUserId(user.id)
                postRepository.deleteAllByUserId(user.id)
                professorRepository.delete(professor)
            }
            ROLE_COMPANY_INSTRUCTOR -> {
                val companyInstructor = companyInstructorRepository findByUser user

                inquiryRepository.deleteAllByUserId(user.id)
                lectureRepository.updateAllByUser(user)
                lectureRepository.deleteAllByUserId(user.id)
                postRepository.deleteAllByUserId(user.id)
                companyInstructorRepository.delete(companyInstructor)
                userRepository.deleteByIdIn(listOf(user.id))
            }
            ROLE_GOVERNMENT -> {
                val government = governmentRepository findByUser user

                inquiryRepository.deleteAllByUserId(user.id)
                lectureRepository.updateAllByUser(user)
                lectureRepository.deleteAllByUserId(user.id)
                postRepository.deleteAllByUserId(user.id)
                governmentRepository.delete(government)
            }

            else -> throw UnApprovedUserException("회원가입 승인 대기 중인 유저입니다. info : [ userId = ${user.id} ]")
        }
    }

    private infix fun StudentRepository.findByUser(user: User): Student =
        this.findByUser(user) ?: throw StudentNotFoundException("존재하지 않는 학생 입니다. info : [ userId = ${user.id} ]")

    private infix fun AdminRepository.findByUser(user: User): Admin =
        this.findByUser(user) ?: throw AdminNotFoundException("존재하지 않는 어드민 입니다. info : [ userId = ${user.id} ]")

    private infix fun BbozzakRepository.findByUser(user: User): Bbozzak =
        this.findByUser(user) ?: throw BbozzakNotFoundException("존재하지 않는 뽀짝샘 입니다. info : [ userId = ${user.id} ]")

    private infix fun TeacherRepository.findByUser(user: User): Teacher =
        this.findByUser(user) ?: throw TeacherNotFoundException("존재하지 않는 취동샘 입니다. info : [ userId = ${user.id} ]")

    private infix fun ProfessorRepository.findByUser(user: User): Professor =
        this.findByUser(user) ?: throw ProfessorNotFoundException("존재하지 않는 대학 교수 입니다. info : [ userId = ${user.id} ]")

    private infix fun CompanyInstructorRepository.findByUser(user: User): CompanyInstructor =
        this.findByUser(user) ?: throw CompanyNotFoundException("존재하지 않는 기업 강사 입니다. info : [ userId = ${user.id} ]")

    private infix fun GovernmentRepository.findByUser(user: User): Government =
        this.findByUser(user) ?: throw GovernmentNotFoundException("존재하지 않는 유관 기관 입니다. info : [ userId = ${user.id} ]")

}