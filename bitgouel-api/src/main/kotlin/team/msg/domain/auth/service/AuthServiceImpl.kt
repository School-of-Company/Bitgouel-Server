package team.msg.domain.auth.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.msg.common.util.SecurityUtil
import team.msg.domain.auth.exception.AlreadyExistEmailException
import team.msg.domain.auth.exception.AlreadyExistPhoneNumberException
import team.msg.domain.auth.presentation.data.request.StudentSignUpRequest
import team.msg.domain.auth.presentation.data.request.TeacherSignUpRequest
import team.msg.domain.club.exception.ClubNotFoundException
import team.msg.domain.club.repository.ClubRepository
import team.msg.domain.school.exception.SchoolNotFoundException
import team.msg.domain.school.repository.SchoolRepository
import team.msg.domain.student.enums.StudentRole
import team.msg.domain.student.model.Student
import team.msg.domain.student.repository.StudentRepository
import team.msg.domain.teacher.model.Teacher
import team.msg.domain.teacher.repository.TeacherRepository
import team.msg.domain.user.enums.Authority
import team.msg.domain.user.enums.SignUpStatus
import team.msg.domain.user.model.User
import team.msg.domain.user.repository.UserRepository
import java.util.*

@Service
class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val securityUtil: SecurityUtil,
    private val clubRepository: ClubRepository,
    private val studentRepository: StudentRepository,
    private val schoolRepository: SchoolRepository,
    private val teacherRepository: TeacherRepository
) : AuthService {

    /**
     * 학생 회원가입을 처리해주는 비지니스 로직입니다.
     * @param StudentSignUpRequest
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun studentSignUp(request: StudentSignUpRequest) {
        val user = createUser(request.email, request.name, request.phoneNumber, request.password)

        val school = schoolRepository.findByHighSchool(request.highSchool)
            ?: throw SchoolNotFoundException("존재하지 않는 학교 입니다. values : [ highSchool = ${request.highSchool} ]")
        val club = clubRepository.findByNameAndSchool(request.clubName, school)
            ?: throw ClubNotFoundException("존재하지 않는 동아리입니다. values : [ club = ${request.clubName} ]")

        val student = Student(
            id = UUID.randomUUID(),
            user = user,
            club = club,
            grade = request.grade,
            classRoom = request.classRoom,
            number = request.number,
            cohort = request.admissionNumber - 2020,
            credit = 0,
            studentRole = StudentRole.STUDENT
        )
        studentRepository.save(student)
    }

    /**
     * 취동샘 회원가입을 처리해주는 비지니스 로직입니다.
     * @param TeacherSignUpRequest
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun teacherSignUp(request: TeacherSignUpRequest) {
        val user = createUser(request.email, request.name, request.phoneNumber, request.password)

        val school = schoolRepository.findByHighSchool(request.highSchool)
            ?: throw SchoolNotFoundException("존재하지 않는 학교 입니다. values : [ highSchool = ${request.highSchool} ]")
        val club = clubRepository.findByNameAndSchool(request.clubName, school)
            ?: throw ClubNotFoundException("존재하지 않는 동아리입니다. values : [ club = ${request.clubName} ]")

        val teacher = Teacher(
            id = UUID.randomUUID(),
            user = user,
            club = club
        )
        teacherRepository.save(teacher)
    }

    /**
     * 유저 회원가입을 처리해주는 private 함수입니다.
     * @param email, name, phoneNumber, password
     */
    private fun createUser(email: String, name: String, phoneNumber: String, password: String): User {
        if (userRepository.existsByEmail(email))
            throw AlreadyExistEmailException("이미 가입된 이메일을 기입하였습니다. info : [ email = $email ]")

        if (userRepository.existsByPhoneNumber(phoneNumber))
            throw AlreadyExistPhoneNumberException("이미 가입된 전화번호를 기입하였습니다. info : [ phoneNumber = $phoneNumber ]")

        val user = User(
            id = UUID.randomUUID(),
            email = email,
            name = name,
            phoneNumber = phoneNumber,
            password = securityUtil.passwordEncode(password),
            authority = Authority.ROLE_STUDENT,
            signUpStatus = SignUpStatus.PENDING
        )

        return userRepository.save(user)
    }
}