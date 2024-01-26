package team.msg.domain.auth.service

import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.context.ApplicationEventPublisher
import team.msg.common.util.SecurityUtil
import team.msg.common.util.UserUtil
import team.msg.domain.auth.presentation.data.request.StudentSignUpRequest
import team.msg.domain.auth.repository.RefreshTokenRepository
import team.msg.domain.bbozzak.repository.BbozzakRepository
import team.msg.domain.club.createClub
import team.msg.domain.club.repository.ClubRepository
import team.msg.domain.company.repository.CompanyInstructorRepository
import team.msg.domain.government.repository.GovernmentRepository
import team.msg.domain.professor.repository.ProfessorRepository
import team.msg.domain.school.createSchool
import team.msg.domain.school.enums.HighSchool
import team.msg.domain.school.repository.SchoolRepository
import team.msg.domain.student.createStudent
import team.msg.domain.student.enums.StudentRole
import team.msg.domain.student.repository.StudentRepository
import team.msg.domain.teacher.repository.TeacherRepository
import team.msg.domain.user.createUser
import team.msg.domain.user.repository.UserRepository
import team.msg.global.security.jwt.JwtTokenGenerator
import team.msg.global.security.jwt.JwtTokenParser
import java.util.*
import com.appmattus.kotlinfixture.kotlinFixture

class AuthServiceImplTest : BehaviorSpec({

    val userRepository = mockk<UserRepository>()
    val securityUtil = mockk<SecurityUtil>()
    val schoolRepository = mockk<SchoolRepository>()
    val clubRepository = mockk<ClubRepository>()
    val studentRepository = mockk<StudentRepository>()
    val teacherRepository = mockk<TeacherRepository>()
    val professorRepository = mockk<ProfessorRepository>()
    val governmentRepository = mockk<GovernmentRepository>()
    val companyInstructorRepository = mockk<CompanyInstructorRepository>()
    val jwtTokenGenerator = mockk<JwtTokenGenerator>()
    val jwtTokenParser = mockk<JwtTokenParser>()
    val refreshTokenRepository = mockk<RefreshTokenRepository>()
    val userUtil = mockk<UserUtil>()
    val applicationEventPublisher = mockk<ApplicationEventPublisher>()
    val bbozzakRepository = mockk<BbozzakRepository>()
    val authServiceImpl = AuthServiceImpl(
        userRepository,
        securityUtil,
        clubRepository,
        studentRepository,
        schoolRepository,
        teacherRepository,
        professorRepository,
        governmentRepository,
        companyInstructorRepository,
        jwtTokenGenerator,
        jwtTokenParser,
        refreshTokenRepository,
        userUtil,
        applicationEventPublisher,
        bbozzakRepository
    )

    val fixture = kotlinFixture()

    Given("StudentSignUpRequest 가 주어지면") {
        val email = "s22046@gsm.hs.kr"
        val name = "박주홍"
        val phoneNumber = "01083149727"
        val encodedPassword = "123456789a@"
        val highSchool = HighSchool.GWANGJU_SOFTWARE_MEISTER_HIGH_SCHOOL
        val clubName = "dev GSM"
        val grade = 2
        val classRoom = 3
        val number = 6
        val admissionNumber = 1

        val user = createUser(
            email = email,
            name = name,
            phoneNumber = phoneNumber,
            password = encodedPassword
        )

        val school = createSchool(
            schoolId = 1L,
            highSchool = highSchool
        )

        val club = createClub(
            clubId = 1L,
            school = school,
            name = clubName
        )

        val student = createStudent(
            studentId = UUID.randomUUID(),
            user = user,
            club = club,
            grade = grade,
            classRoom = classRoom,
            number = number,
            cohort = admissionNumber,
            credit = 0,
            studentRole = StudentRole.STUDENT
        )

        every { userRepository.existsByEmail(user.email) } returns false
        every { userRepository.existsByPhoneNumber(user.phoneNumber) } returns false
        every { schoolRepository.findByHighSchool(highSchool) } returns school
        every { clubRepository.findByNameAndSchool(clubName, school) } returns club
        every { securityUtil.passwordEncode(any()) } answers { encodedPassword }
        every { studentRepository.save(any()) } returns student

        When("학생 회원가입 요청을 하면") {

            authServiceImpl.studentSignUp(StudentSignUpRequest(
                email = user.email,
                name = user.name,
                phoneNumber = user.phoneNumber,
                password = user.password,
                highSchool = highSchool,
                clubName, grade, classRoom, number, admissionNumber
            ))

            Then("User 가 저장이 되어야 한다.") {
                verify(exactly = 1) { studentRepository.save(any()) }
            }
        }
    }
})