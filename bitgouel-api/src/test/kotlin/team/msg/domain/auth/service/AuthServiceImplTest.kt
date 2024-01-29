package team.msg.domain.auth.service

import com.appmattus.kotlinfixture.kotlinFixture
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.context.ApplicationEventPublisher
import team.msg.common.util.SecurityUtil
import team.msg.common.util.UserUtil
import team.msg.domain.auth.exception.AlreadyExistEmailException
import team.msg.domain.auth.exception.AlreadyExistPhoneNumberException
import team.msg.domain.auth.presentation.data.request.StudentSignUpRequest
import team.msg.domain.auth.presentation.data.request.TeacherSignUpRequest
import team.msg.domain.auth.repository.RefreshTokenRepository
import team.msg.domain.bbozzak.repository.BbozzakRepository
import team.msg.domain.club.exception.ClubNotFoundException
import team.msg.domain.club.model.Club
import team.msg.domain.club.repository.ClubRepository
import team.msg.domain.company.repository.CompanyInstructorRepository
import team.msg.domain.government.repository.GovernmentRepository
import team.msg.domain.professor.repository.ProfessorRepository
import team.msg.domain.school.enums.HighSchool
import team.msg.domain.school.exception.SchoolNotFoundException
import team.msg.domain.school.model.School
import team.msg.domain.school.repository.SchoolRepository
import team.msg.domain.student.model.Student
import team.msg.domain.student.repository.StudentRepository
import team.msg.domain.teacher.model.Teacher
import team.msg.domain.teacher.repository.TeacherRepository
import team.msg.domain.user.model.User
import team.msg.domain.user.repository.UserRepository
import team.msg.global.security.jwt.JwtTokenGenerator
import team.msg.global.security.jwt.JwtTokenParser

class AuthServiceImplTest : BehaviorSpec({

    isolationMode = IsolationMode.InstancePerLeaf
    val fixture = kotlinFixture()

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

    // studentSignUp 테스트 코드
    Given("StudentSignUpRequest 가 주어지면") {
        val email = "s22046@gsm.hs.kr"
        val phoneNumber = "01083149727"
        val encodedPassword = "123456789a@"
        val highSchool = HighSchool.GWANGJU_SOFTWARE_MEISTER_HIGH_SCHOOL
        val clubName = "dev GSM"

        val request = fixture<StudentSignUpRequest> {
            property(StudentSignUpRequest::email) { email }
            property(StudentSignUpRequest::phoneNumber) { phoneNumber }
            property(StudentSignUpRequest::highSchool) { highSchool }
            property(StudentSignUpRequest::clubName) { clubName }
        }
        val user = fixture<User>()
        val school = fixture<School>()
        val club = fixture<Club>()
        val student = fixture<Student>()

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

        When("이미 존재하는 이메일로 학생 회원가입 요청을 하면") {
            every { userRepository.existsByEmail(request.email) } returns true

            Then("AlreadyExistEmailException 가 터져야 한다.") {
                shouldThrow<AlreadyExistEmailException> {
                    authServiceImpl.studentSignUp(request)
                }
            }
        }

        When("이미 존재하는 전화번호로 학생 회원가입 요청을 하면") {
            every { userRepository.existsByPhoneNumber(request.phoneNumber) } returns true

            Then("AlreadyExistPhoneNumberException 가 터져야 한다.") {
                shouldThrow<AlreadyExistPhoneNumberException> {
                    authServiceImpl.studentSignUp(request)
                }
            }
        }

        When("존재하지 않는 학교로 학생 회원가입 요청을 하면") {
            every { schoolRepository.findByHighSchool(request.highSchool) } returns null

            Then("SchoolNotFoundException 가 터져야 한다.") {
                shouldThrow<SchoolNotFoundException> {
                    authServiceImpl.studentSignUp(request)
                }
            }
        }

        When("존재하지 않는 동아리와 학교로 학생 회원가입 요청을 하면") {
            every { clubRepository.findByNameAndSchool(request.clubName, school) } returns null

            Then("ClubNotFoundException 가 터져야 한다.") {
                shouldThrow<ClubNotFoundException> {
                    authServiceImpl.studentSignUp(request)
                }
            }
        }
    }

    // studentSignUp 테스트 코드
    Given("TeacherSignUpRequest 가 주어지면") {
        val email = "s22046@gsm.hs.kr"
        val phoneNumber = "01083149727"
        val encodedPassword = "123456789a@"
        val highSchool = HighSchool.GWANGJU_SOFTWARE_MEISTER_HIGH_SCHOOL
        val clubName = "dev GSM"

        val request = fixture<TeacherSignUpRequest> {
            property(StudentSignUpRequest::email) { email }
            property(StudentSignUpRequest::phoneNumber) { phoneNumber }
            property(StudentSignUpRequest::highSchool) { highSchool }
            property(StudentSignUpRequest::clubName) { clubName }
        }
        val user = fixture<User>()
        val school = fixture<School>()
        val club = fixture<Club>()
        val teacher = fixture<Teacher>()

        every { userRepository.existsByEmail(request.email) } returns false
        every { userRepository.existsByPhoneNumber(request.phoneNumber) } returns false
        every { schoolRepository.findByHighSchool(request.highSchool) } returns school
        every { clubRepository.findByNameAndSchool(request.clubName, school) } returns club
        every { securityUtil.passwordEncode(any()) } returns encodedPassword
        every { teacherRepository.save(any()) } returns teacher

        When("선생 회원가입 요청을 하면") {
            authServiceImpl.teacherSignUp(request)

            Then("Teacher 가 저장이 되어야 한다.") {
                verify(exactly = 0) { userRepository.save(any()) }
                verify(exactly = 1) { teacherRepository.save(any()) }
            }
        }

        When("이미 존재하는 이메일로 선생님 회원가입 요청을 하면") {
            every { userRepository.existsByEmail(request.email) } returns true

            Then("AlreadyExistEmailException 가 터져야 한다.") {
                shouldThrow<AlreadyExistEmailException> {
                    authServiceImpl.teacherSignUp(request)
                }
            }
        }

        When("이미 존재하는 전화번호로 선생님 회원가입 요청을 하면") {
            every { userRepository.existsByPhoneNumber(request.phoneNumber) } returns true

            Then("AlreadyExistPhoneNumberException 가 터져야 한다.") {
                shouldThrow<AlreadyExistPhoneNumberException> {
                    authServiceImpl.teacherSignUp(request)
                }
            }
        }

        When("존재하지 않는 학교로 선생님 회원가입 요청을 하면") {
            every { schoolRepository.findByHighSchool(request.highSchool) } returns null

            Then("SchoolNotFoundException 가 터져야 한다.") {
                shouldThrow<SchoolNotFoundException> {
                    authServiceImpl.teacherSignUp(request)
                }
            }
        }

        When("존재하지 않는 동아리와 학교로 선생 회원가입 요청을 하면") {
            every { clubRepository.findByNameAndSchool(request.clubName, school) } returns null

            Then("ClubNotFoundException 가 터져야 한다.") {
                shouldThrow<ClubNotFoundException> {
                    authServiceImpl.teacherSignUp(request)
                }
            }
        }
    }
})