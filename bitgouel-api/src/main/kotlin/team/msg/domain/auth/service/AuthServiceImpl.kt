package team.msg.domain.auth.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.msg.common.enum.ApproveStatus
import team.msg.common.util.SecurityUtil
import team.msg.common.util.UserUtil
import team.msg.domain.auth.exception.AlreadyExistEmailException
import team.msg.domain.auth.exception.AlreadyExistPhoneNumberException
import team.msg.domain.auth.exception.InvalidRefreshTokenException
import team.msg.domain.auth.exception.MisMatchPasswordException
import team.msg.domain.auth.exception.RefreshTokenNotFoundException
import team.msg.domain.auth.exception.UnApprovedUserException
import team.msg.domain.auth.presentation.data.request.*
import team.msg.domain.auth.presentation.data.response.TokenResponse
import team.msg.domain.auth.repository.RefreshTokenRepository
import team.msg.domain.club.exception.ClubNotFoundException
import team.msg.domain.club.model.Club
import team.msg.domain.club.repository.ClubRepository
import team.msg.domain.company.model.CompanyInstructor
import team.msg.domain.company.repository.CompanyInstructorRepository
import team.msg.domain.government.model.Government
import team.msg.domain.government.repository.GovernmentRepository
import team.msg.domain.professor.model.Professor
import team.msg.domain.professor.repository.ProfessorRepository
import team.msg.domain.school.enums.HighSchool
import team.msg.domain.school.exception.SchoolNotFoundException
import team.msg.domain.school.repository.SchoolRepository
import team.msg.domain.student.enums.StudentRole
import team.msg.domain.student.model.Student
import team.msg.domain.student.repository.StudentRepository
import team.msg.domain.teacher.model.Teacher
import team.msg.domain.teacher.repository.TeacherRepository
import team.msg.domain.user.enums.Authority
import team.msg.domain.user.exception.UserNotFoundException
import team.msg.domain.user.model.User
import team.msg.domain.user.repository.UserRepository
import team.msg.global.security.jwt.JwtTokenGenerator
import team.msg.global.security.jwt.JwtTokenParser
import java.util.*

@Service
class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val securityUtil: SecurityUtil,
    private val clubRepository: ClubRepository,
    private val studentRepository: StudentRepository,
    private val schoolRepository: SchoolRepository,
    private val teacherRepository: TeacherRepository,
    private val professorRepository: ProfessorRepository,
    private val governmentRepository: GovernmentRepository,
    private val companyInstructorRepository: CompanyInstructorRepository,
    private val jwtTokenGenerator: JwtTokenGenerator,
    private val jwtTokenParser: JwtTokenParser,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val userUtil: UserUtil
) : AuthService {

    /**
     * 학생 회원가입을 처리하는 비지니스 로직입니다.
     * @param StudentSignUpRequest
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun studentSignUp(request: StudentSignUpRequest) {
        val user = createUser(
            request.email,
            request.name,
            request.phoneNumber,
            request.password,
            Authority.ROLE_STUDENT
        )

        val club = queryClub(request.highSchool, request.clubName)

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
     * 취동샘 회원가입을 처리하는 비지니스 로직입니다.
     * @param TeacherSignUpRequest
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun teacherSignUp(request: TeacherSignUpRequest) {
        val user = createUser(
            request.email,
            request.name,
            request.phoneNumber,
            request.password,
            Authority.ROLE_TEACHER
        )

        val club = queryClub(request.highSchool, request.clubName)

        val teacher = Teacher(
            id = UUID.randomUUID(),
            user = user,
            club = club
        )
        teacherRepository.save(teacher)
    }

    /**
     * 대학교수 회원가입을 처리하는 비지니스 로직입니다.
     * @param ProfessorSignUpRequest
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun professorSignUp(request: ProfessorSignUpRequest) {
        val user = createUser(
            request.email,
            request.name,
            request.phoneNumber,
            request.password,
            Authority.ROLE_PROFESSOR
        )

        val club = queryClub(request.highSchool, request.clubName)

        val professor = Professor(
            id = UUID.randomUUID(),
            user = user,
            club = club,
            university = request.university
        )
        professorRepository.save(professor)
    }

    /**
     * 유관 기관 회원가입을 처리하는 비지니스 로직입니다.
     * @param GovernmentSignUpRequest
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun governmentSignUp(request: GovernmentSignUpRequest) {
        val user = createUser(
            request.email,
            request.name,
            request.phoneNumber,
            request.password,
            Authority.ROLE_GOVERNMENT
        )

        val club = queryClub(request.highSchool, request.clubName)

        val government = Government(
            id = UUID.randomUUID(),
            user = user,
            club = club,
            governmentName = request.governmentName
        )
        governmentRepository.save(government)
    }

    /**
     * 기업 강사 회원가입을 처리하는 비지니스 로직입니다.
     * @param CompanyInstructorSignUpRequest
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun companyInstructorSignUp(request: CompanyInstructorSignUpRequest) {
        val user = createUser(request.email, request.name, request.phoneNumber, request.password, Authority.ROLE_COMPANY_INSTRUCTOR)

        val club = queryClub(request.highSchool, request.clubName)

        val companyInstructor = CompanyInstructor(
            id = UUID.randomUUID(),
            user = user,
            club = club,
            company = request.company
        )
        companyInstructorRepository.save(companyInstructor)
    }

    /**
     * 로그인을 처리하는 비지니스 로직입니다.
     * @param LoginRequest
     */
    @Transactional(rollbackFor = [Exception::class], readOnly = true)
    override fun login(request: LoginRequest): TokenResponse {
        val user = userRepository.findByEmail(request.email)
            ?: throw UserNotFoundException("존재하지 않는 유저입니다.")

        if (!securityUtil.isPasswordMatch(request.password, user.password))
            throw MisMatchPasswordException("비말번호가 일치하지 않습니다. info : [ password = ${request.password} ]")

        if (user.approveStatus == ApproveStatus.PENDING)
            throw UnApprovedUserException("아직 회원가입 대기 중인 유저입니다. info : [ user = ${user.name} ]")

        return jwtTokenGenerator.generateToken(user.id, user.authority)
    }

    /**
     * 토큰 재발급을 처리하는 메서드입니다.
     * @param requestToken
     */
    @Transactional(rollbackFor = [Exception::class], readOnly = true)
    override fun reissueToken(requestToken: String): TokenResponse {
        val refreshToken = jwtTokenParser.parseRefreshToken(requestToken)
            ?: throw InvalidRefreshTokenException("유효하지 않은 리프레시 토큰입니다. info : [ refreshToken = $requestToken ]")

        val token = refreshTokenRepository.findByIdOrNull(refreshToken)
            ?: throw RefreshTokenNotFoundException("존재하지 않는 리프레시 토큰입니다. info : [ refreshToken = $refreshToken ]")

        val user = userRepository.findByIdOrNull(token.userId)
            ?: throw UserNotFoundException("존재하지 않는 유저입니다. info : [ userId = ${token.userId} ]")

        return jwtTokenGenerator.generateToken(user.id, user.authority)
    }

    /**
     * 로그아웃을 처리하는 메서드입니다.
     * @param requestToken
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun logout(requestToken: String) {
        val user = userUtil.queryCurrentUser()

        val refreshToken = jwtTokenParser.parseRefreshToken(requestToken)
            ?: throw InvalidRefreshTokenException("유효하지 않은 리프레시 토큰입니다. info : [ refreshToken = $requestToken ]")

        val token = refreshTokenRepository.findByIdOrNull(refreshToken)
            ?: throw RefreshTokenNotFoundException("존재하지 않는 리프레시 토큰입니다. info : [ refreshToken = $refreshToken ]")

        if (token.userId != user.id)
            throw UserNotFoundException("존재하지 않는 유저입니다. info : [ userId =  ${token.userId} ]")

        refreshTokenRepository.delete(token)
    }

    /**
     * 유저 생성과 검증을 처리하는 private 메서드입니다.
     * @param email, name, phoneNumber, password, authority
     */
    private fun createUser(email: String, name: String, phoneNumber: String, password: String, authority: Authority): User {
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
            authority = authority,
            approveStatus = ApproveStatus.PENDING
        )

        return userRepository.save(user)
    }

    /**
     * 학교와 동아리 이름을 받아 동아리 객체를 리턴하는 private 메서드입니다.
     * @param highSchool, clubName
     */
    private fun queryClub(highSchool: HighSchool, clubName: String): Club {
        val school = schoolRepository.findByHighSchool(highSchool)
            ?: throw SchoolNotFoundException("존재하지 않는 학교입니다. info : [ highSchool = $highSchool ]")

        val club = clubRepository.findByNameAndSchool(clubName, school)
            ?: throw ClubNotFoundException("존재하지 않는 동아리입니다. info : [ club = $clubName ]")

        return club
    }
}