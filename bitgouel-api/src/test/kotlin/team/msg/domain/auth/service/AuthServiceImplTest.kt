package team.msg.domain.auth.service

import com.appmattus.kotlinfixture.kotlinFixture
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.ints.exactly
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.context.ApplicationEventPublisher
import team.msg.common.util.SecurityUtil
import team.msg.common.util.UserUtil
import team.msg.domain.auth.presentation.data.request.StudentSignUpRequest
import team.msg.domain.auth.repository.RefreshTokenRepository
import team.msg.domain.bbozzak.repository.BbozzakRepository
import team.msg.domain.club.model.Club
import team.msg.domain.club.repository.ClubRepository
import team.msg.domain.company.repository.CompanyInstructorRepository
import team.msg.domain.government.repository.GovernmentRepository
import team.msg.domain.professor.repository.ProfessorRepository
import team.msg.domain.school.enums.HighSchool
import team.msg.domain.school.model.School
import team.msg.domain.school.repository.SchoolRepository
import team.msg.domain.student.model.Student
import team.msg.domain.student.repository.StudentRepository
import team.msg.domain.teacher.repository.TeacherRepository
import team.msg.domain.user.model.User
import team.msg.domain.user.repository.UserRepository
import team.msg.global.security.jwt.JwtTokenGenerator
import team.msg.global.security.jwt.JwtTokenParser

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
        val phoneNumber = "01083149727"
        val encodedPassword = "123456789a@"
        val highSchool = HighSchool.GWANGJU_SOFTWARE_MEISTER_HIGH_SCHOOL
        val clubName = "dev GSM"

        val request = fixture<StudentSignUpRequest> {
            property(StudentSignUpRequest::email) { "s22046@gsm.hs.kr" }
            property(StudentSignUpRequest::phoneNumber) { "01083149727" }
            property(StudentSignUpRequest::password) { "123456789a@" }
            property(StudentSignUpRequest::highSchool) { HighSchool.GWANGJU_SOFTWARE_MEISTER_HIGH_SCHOOL }
            property(StudentSignUpRequest::clubName) { "dev GSM" }
        }
        val user = fixture<User> {
            property(User::email) { email }
            property(User::phoneNumber) { phoneNumber }
            property(User::password) { encodedPassword }
        }
        val school = fixture<School> {
            property(School::highSchool) { highSchool }
        }
        val club = fixture<Club> {
            property(Club::school) { school }
            property(Club::name) { clubName }
        }
        val student = fixture<Student> {
            property(Student::user) { user }
        }

        every { userRepository.existsByEmail(request.email) } returns false
        every { userRepository.existsByPhoneNumber(request.phoneNumber) } returns false
        every { schoolRepository.findByHighSchool(request.highSchool) } returns school
        every { clubRepository.findByNameAndSchool(request.clubName, school) } returns club
        every { securityUtil.passwordEncode(any()) } returns encodedPassword
        every { studentRepository.save(any()) } returns student

        When("학생 회원가입 요청을 하면") {
            authServiceImpl.studentSignUp(request)

            Then("Student 가 저장이 되어야 한다.") {
                verify(exactly = 0) { userRepository.save(any()) }
                verify(exactly = 1) { studentRepository.save(any()) }
            }
        }
    }
})