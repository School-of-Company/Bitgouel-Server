package team.msg.domain.auth.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.msg.common.util.SecurityUtil
import team.msg.domain.auth.exception.AlreadyExistEmailException
import team.msg.domain.auth.exception.AlreadyExistPhoneNumberException
import team.msg.domain.auth.presentation.data.request.StudentSignUpRequest
import team.msg.domain.club.exception.ClubNotFoundException
import team.msg.domain.club.repository.ClubRepository
import team.msg.domain.school.exception.SchoolNotFoundException
import team.msg.domain.school.repository.SchoolRepository
import team.msg.domain.student.enums.StudentRole
import team.msg.domain.student.model.Student
import team.msg.domain.student.repository.StudentRepository
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
    private val schoolRepository: SchoolRepository
) : AuthService {

    @Transactional(rollbackFor = [Exception::class])
    override fun studentSignUp(request: StudentSignUpRequest) {
        val email = request.email
        val phoneNumber = request.phoneNumber

        if (userRepository.existsByEmail(email))
            throw AlreadyExistEmailException("이미 가입된 이메일을 기입하였습니다. info : [ email = $email ]")

        if (userRepository.existsByPhoneNumber(phoneNumber))
            throw AlreadyExistPhoneNumberException("이미 가입된 전화번호를 기입하였습니다. info : [ phoneNumber = $phoneNumber ]")

        val user = User(
            id = UUID.randomUUID(),
            email = email,
            name = request.name,
            phoneNumber = phoneNumber,
            password = securityUtil.passwordEncode(request.password),
            authority = Authority.ROLE_STUDENT,
            signUpStatus = SignUpStatus.PENDING
        )
        userRepository.save(user)

        val school = schoolRepository.findByHighSchool(request.highSchool)
            ?: throw SchoolNotFoundException("존재하지 않는 학교 입니다. values = [ highSchool = ${request.highSchool} ]")
        val club = clubRepository.findByNameAndSchool(request.clubName, school)
            ?: throw ClubNotFoundException("존재하지 않는 동아리입니다. values = [ club = ${request.clubName} ]")

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
}