package team.msg.common.util

import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import team.msg.common.entity.BaseUUIDEntity
import team.msg.common.enums.ApproveStatus
import team.msg.domain.admin.exception.AdminNotFoundException
import team.msg.domain.admin.model.Admin
import team.msg.domain.admin.repository.AdminRepository
import team.msg.domain.auth.exception.AlreadyExistEmailException
import team.msg.domain.auth.exception.AlreadyExistPhoneNumberException
import team.msg.domain.auth.exception.UnApprovedUserException
import team.msg.domain.bbozzak.exception.BbozzakNotFoundException
import team.msg.domain.bbozzak.model.Bbozzak
import team.msg.domain.bbozzak.repository.BbozzakRepository
import team.msg.domain.certifiacation.repository.CertificationRepository
import team.msg.domain.company.exception.CompanyInstructorNotFoundException
import team.msg.domain.company.model.CompanyInstructor
import team.msg.domain.company.repository.CompanyInstructorRepository
import team.msg.domain.government.exception.GovernmentInstructorNotFoundException
import team.msg.domain.government.model.GovernmentInstructor
import team.msg.domain.government.repository.GovernmentInstructorRepository
import team.msg.domain.inquiry.repository.InquiryAnswerRepository
import team.msg.domain.inquiry.repository.InquiryRepository
import team.msg.domain.lecture.repository.LectureRepository
import team.msg.domain.lecture.repository.RegisteredLectureRepository
import team.msg.domain.post.repository.PostRepository
import team.msg.domain.university.exception.ProfessorNotFoundException
import team.msg.domain.university.model.Professor
import team.msg.domain.university.repository.ProfessorRepository
import team.msg.domain.student.exception.StudentNotFoundException
import team.msg.domain.student.model.Student
import team.msg.domain.student.repository.StudentActivityRepository
import team.msg.domain.student.repository.StudentRepository
import team.msg.domain.teacher.exception.TeacherNotFoundException
import team.msg.domain.teacher.model.Teacher
import team.msg.domain.teacher.repository.TeacherRepository
import team.msg.domain.user.enums.Authority
import team.msg.domain.user.exception.UserNotFoundException
import team.msg.domain.user.model.User
import team.msg.domain.user.repository.UserRepository
import team.msg.domain.withdraw.repository.WithdrawStudentRepository
import team.msg.global.exception.InvalidRoleException
import team.msg.global.security.principal.AuthDetails
import java.util.*

@Component
class UserUtil(
    private val userRepository: UserRepository,
    private val studentRepository: StudentRepository,
    private val teacherRepository: TeacherRepository,
    private val bbozzakRepository: BbozzakRepository,
    private val professorRepository: ProfessorRepository,
    private val companyInstructorRepository: CompanyInstructorRepository,
    private val governmentInstructorRepository: GovernmentInstructorRepository,
    private val adminRepository: AdminRepository,
    private val studentActivityRepository: StudentActivityRepository,
    private val registeredLectureRepository: RegisteredLectureRepository,
    private val lectureRepository: LectureRepository,
    private val certificationRepository: CertificationRepository,
    private val postRepository: PostRepository,
    private val withdrawStudentRepository: WithdrawStudentRepository,
    private val inquiryRepository: InquiryRepository,
    private val inquiryAnswerRepository: InquiryAnswerRepository,
    private val securityUtil: SecurityUtil
) {

    fun queryCurrentUserId(): UUID {
        val userId = when (val principal = SecurityContextHolder.getContext().authentication.principal) {
            is AuthDetails -> UUID.fromString(principal.username)
            else -> UUID.fromString(principal.toString())
        }

        return userId
    }

    fun queryCurrentUser(): User {
        val userId = queryCurrentUserId()

        val user = userRepository.findByIdOrNull(userId)
            ?: throw UserNotFoundException("존재하지 않는 유저입니다. : [ id = $userId ]")

        return user
    }

    /**
     * 유저의 권한에 따라 권한 엔티티를 반환하는 함수입니다.
     * @param 권한 엔티티를 얻고자 하는 유저
     * @return 유저에 따른 권한 엔티티
     */
    fun getAuthorityEntityAndOrganization(user: User): Pair<BaseUUIDEntity, String> {
        return when (user.authority) {
            Authority.ROLE_STUDENT -> {
                val student = studentRepository findByUser user
                val club = student.club
                val school = club.school
                val organization = "${school.name}/${club.name}/${student.grade}학년 ${student.classRoom}반 ${student.number}번"
                Pair(student, organization)
            }
            Authority.ROLE_TEACHER -> {
                val teacher = teacherRepository findByUser user
                val club = teacher.club
                val school = club.school
                val organization = "${school.name}/${club.name}"
                Pair(teacher, organization)
            }
            Authority.ROLE_BBOZZAK -> {
                val bbozzak = bbozzakRepository findByUser user
                val club = bbozzak.club
                val school = club.school
                val organization = "${school.name}/${club.name}"
                Pair(bbozzak, organization)
            }
            Authority.ROLE_PROFESSOR -> {
                val professor = professorRepository findByUser user
                val organization = professor.university.name
                Pair(professor, organization)
            }
            Authority.ROLE_COMPANY_INSTRUCTOR -> {
                val companyInstructor = companyInstructorRepository findByUser user
                val organization = companyInstructor.company.name
                Pair(companyInstructor, organization)
            }
            Authority.ROLE_GOVERNMENT -> {
                val governmentInstructor = governmentInstructorRepository findByUser user
                val organization = governmentInstructor.government.name
                Pair(governmentInstructor, organization)
            }
            Authority.ROLE_ADMIN -> {
                val admin = adminRepository findByUser user
                val organization = "교육청"
                Pair(admin, organization)
            }
            else -> throw InvalidRoleException("유효하지 않은 권한입니다. info : [ userAuthority = ${user.authority}]")
        }
    }

    fun withdrawUser(user: User) {

        when (user.authority) {
            Authority.ROLE_STUDENT -> {
                val student = studentRepository findByUser user

                inquiryRepository.deleteAllByUserId(user.id)
                studentActivityRepository.deleteAllByStudentId(student.id)
                registeredLectureRepository.deleteAllByStudentId(student.id)
                certificationRepository.deleteAllByStudentId(student.id)
                withdrawStudentRepository.deleteByStudent(student)
                studentRepository.delete(student)
            }
            Authority.ROLE_ADMIN -> {
                val admin = adminRepository findByUser user

                inquiryAnswerRepository.deleteAllByAdminId(admin.id)
                postRepository.deleteAllByUserId(user.id)
                adminRepository.delete(admin)
            }
            Authority.ROLE_BBOZZAK -> {
                val bbozzak = bbozzakRepository findByUser user

                inquiryRepository.deleteAllByUserId(user.id)
                postRepository.deleteAllByUserId(user.id)
                bbozzakRepository.delete(bbozzak)
            }
            Authority.ROLE_TEACHER -> {
                val teacher = teacherRepository findByUser user

                inquiryRepository.deleteAllByUserId(user.id)
                teacherRepository.delete(teacher)
            }
            Authority.ROLE_PROFESSOR -> {
                val professor = professorRepository findByUser user

                inquiryRepository.deleteAllByUserId(user.id)
                lectureRepository.deleteAllByUserId(user.id)
                postRepository.deleteAllByUserId(user.id)
                professorRepository.delete(professor)
            }
            Authority.ROLE_COMPANY_INSTRUCTOR -> {
                val companyInstructor = companyInstructorRepository findByUser user

                inquiryRepository.deleteAllByUserId(user.id)
                lectureRepository.deleteAllByUserId(user.id)
                postRepository.deleteAllByUserId(user.id)
                companyInstructorRepository.delete(companyInstructor)
                userRepository.deleteByIdIn(listOf(user.id))
            }
            Authority.ROLE_GOVERNMENT -> {
                val government = governmentInstructorRepository findByUser user

                inquiryRepository.deleteAllByUserId(user.id)
                lectureRepository.deleteAllByUserId(user.id)
                postRepository.deleteAllByUserId(user.id)
                governmentInstructorRepository.delete(government)
            }

            else -> throw UnApprovedUserException("회원가입 승인 대기 중인 유저입니다. info : [ userId = ${user.id} ]")
        }
    }

    /**
     * 유저 생성과 검증을 처리하는 private 메서드입니다.
     * @param 유저 생성 및 검증하기 위한 email, name, phoneNumber, password, authority 입니다.
     */
    fun createUser(email: String, name: String, phoneNumber: String, password: String, authority: Authority): User {
        if (userRepository.existsByEmail(email))
            throw AlreadyExistEmailException("이미 가입된 이메일을 기입하였습니다. info : [ email = $email ]")

        if (userRepository.existsByPhoneNumber(phoneNumber))
            throw AlreadyExistPhoneNumberException("이미 가입된 전화번호를 기입하였습니다. info : [ phoneNumber = $phoneNumber ]")

        return userRepository.save(
            User(
                id = UUID(0, 0),
                email = email,
                name = name,
                phoneNumber = phoneNumber,
                password = securityUtil.passwordEncode(password),
                authority = authority,
                approveStatus = ApproveStatus.PENDING
            )
        )
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
        this.findByUser(user) ?: throw CompanyInstructorNotFoundException("존재하지 않는 기업 강사 입니다. info : [ userId = ${user.id} ]")

    private infix fun GovernmentInstructorRepository.findByUser(user: User): GovernmentInstructor =
        this.findByUser(user) ?: throw GovernmentInstructorNotFoundException("존재하지 않는 유관 기관 입니다. info : [ userId = ${user.id} ]")
}