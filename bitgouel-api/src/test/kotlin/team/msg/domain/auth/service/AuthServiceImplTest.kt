package team.msg.domain.auth.service

import com.appmattus.kotlinfixture.kotlinFixture
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.repository.findByIdOrNull
import team.msg.common.enums.ApproveStatus
import team.msg.common.util.SecurityUtil
import team.msg.common.util.UserUtil
import team.msg.domain.auth.exception.*
import team.msg.domain.auth.model.RefreshToken
import team.msg.domain.auth.presentation.data.request.*
import team.msg.domain.auth.presentation.data.response.TokenResponse
import team.msg.domain.auth.repository.RefreshTokenRepository
import team.msg.domain.bbozzak.model.Bbozzak
import team.msg.domain.bbozzak.repository.BbozzakRepository
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
import team.msg.domain.school.model.School
import team.msg.domain.school.repository.SchoolRepository
import team.msg.domain.student.model.Student
import team.msg.domain.student.repository.StudentRepository
import team.msg.domain.teacher.model.Teacher
import team.msg.domain.teacher.repository.TeacherRepository
import team.msg.domain.user.event.WithdrawUserEvent
import team.msg.domain.user.exception.UserNotFoundException
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
        val encodedPassword = "123456789a@"

        val request = fixture<StudentSignUpRequest>()
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

    // teacherSignUp 테스트 코드
    Given("TeacherSignUpRequest 가 주어지면") {
        val encodedPassword = "123456789a@"

        val request = fixture<TeacherSignUpRequest>()
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

    // bbozzakSignUp 테스트 코드
    Given("BbozzakSignUpRequest 가 주어지면") {
        val encodedPassword = "123456789a@"

        val request = fixture<BbozzakSignUpRequest>()
        val school = fixture<School>()
        val club = fixture<Club>()
        val bbozzak = fixture<Bbozzak>()

        every { userRepository.existsByEmail(request.email) } returns false
        every { userRepository.existsByPhoneNumber(request.phoneNumber) } returns false
        every { schoolRepository.findByHighSchool(request.highSchool) } returns school
        every { clubRepository.findByNameAndSchool(request.clubName, school) } returns club
        every { securityUtil.passwordEncode(any()) } returns encodedPassword
        every { bbozzakRepository.save(any()) } returns bbozzak

        When("뽀짝 선생님 회원가입 요청을 하면") {
            authServiceImpl.bbozzakSignUp(request)

            Then("Bbozzak 이 저장이 되어야 한다.") {
                verify(exactly = 0) { userRepository.save(any()) }
                verify(exactly = 1) { bbozzakRepository.save(any()) }
            }
        }

        When("이미 존재하는 이메일로 뽀짝 선생님 회원가입 요청을 하면") {
            every { userRepository.existsByEmail(request.email) } returns true

            Then("AlreadyExistEmailException 가 터져야 한다.") {
                shouldThrow<AlreadyExistEmailException> {
                    authServiceImpl.bbozzakSignUp(request)
                }
            }
        }

        When("이미 존재하는 전화번호로 뽀짝 선생님 회원가입 요청을 하면") {
            every { userRepository.existsByPhoneNumber(request.phoneNumber) } returns true

            Then("AlreadyExistPhoneNumberException 가 터져야 한다.") {
                shouldThrow<AlreadyExistPhoneNumberException> {
                    authServiceImpl.bbozzakSignUp(request)
                }
            }
        }

        When("존재하지 않는 학교로 뽀짝 선생님 회원가입 요청을 하면") {
            every { schoolRepository.findByHighSchool(request.highSchool) } returns null

            Then("SchoolNotFoundException 가 터져야 한다.") {
                shouldThrow<SchoolNotFoundException> {
                    authServiceImpl.bbozzakSignUp(request)
                }
            }
        }

        When("존재하지 않는 동아리와 학교로 뽀짝 선생 회원가입 요청을 하면") {
            every { clubRepository.findByNameAndSchool(request.clubName, school) } returns null

            Then("ClubNotFoundException 가 터져야 한다.") {
                shouldThrow<ClubNotFoundException> {
                    authServiceImpl.bbozzakSignUp(request)
                }
            }
        }
    }

    // professorSignUp 테스트 코드
    Given("ProfessorSignUpRequest 가 주어지면") {
        val encodedPassword = "123456789a@"

        val request = fixture<ProfessorSignUpRequest>()
        val school = fixture<School>()
        val club = fixture<Club>()
        val professor = fixture<Professor>()

        every { userRepository.existsByEmail(request.email) } returns false
        every { userRepository.existsByPhoneNumber(request.phoneNumber) } returns false
        every { schoolRepository.findByHighSchool(request.highSchool) } returns school
        every { clubRepository.findByNameAndSchool(request.clubName, school) } returns club
        every { securityUtil.passwordEncode(any()) } returns encodedPassword
        every { professorRepository.save(any()) } returns professor

        When("대학교수 회원가입 요청을 하면") {
            authServiceImpl.professorSignUp(request)

            Then("Professor 가 저장이 되어야 한다.") {
                verify(exactly = 0) { userRepository.save(any()) }
                verify(exactly = 1) { professorRepository.save(any()) }
            }
        }

        When("이미 존재하는 이메일로 대학교수 회원가입 요청을 하면") {
            every { userRepository.existsByEmail(request.email) } returns true

            Then("AlreadyExistEmailException 가 터져야 한다.") {
                shouldThrow<AlreadyExistEmailException> {
                    authServiceImpl.professorSignUp(request)
                }
            }
        }

        When("이미 존재하는 전화번호로 대학교수 회원가입 요청을 하면") {
            every { userRepository.existsByPhoneNumber(request.phoneNumber) } returns true

            Then("AlreadyExistPhoneNumberException 가 터져야 한다.") {
                shouldThrow<AlreadyExistPhoneNumberException> {
                    authServiceImpl.professorSignUp(request)
                }
            }
        }

        When("존재하지 않는 학교로 대학교수 회원가입 요청을 하면") {
            every { schoolRepository.findByHighSchool(request.highSchool) } returns null

            Then("SchoolNotFoundException 가 터져야 한다.") {
                shouldThrow<SchoolNotFoundException> {
                    authServiceImpl.professorSignUp(request)
                }
            }
        }

        When("존재하지 않는 동아리와 학교로 대학교수 회원가입 요청을 하면") {
            every { clubRepository.findByNameAndSchool(request.clubName, school) } returns null

            Then("ClubNotFoundException 가 터져야 한다.") {
                shouldThrow<ClubNotFoundException> {
                    authServiceImpl.professorSignUp(request)
                }
            }
        }
    }

    // governmentSignUp 테스트 코드
    Given("GovernmentSignUpRequest 가 주어지면") {
        val encodedPassword = "123456789a@"

        val request = fixture<GovernmentSignUpRequest>()
        val school = fixture<School>()
        val club = fixture<Club>()
        val government = fixture<Government>()

        every { userRepository.existsByEmail(request.email) } returns false
        every { userRepository.existsByPhoneNumber(request.phoneNumber) } returns false
        every { schoolRepository.findByHighSchool(request.highSchool) } returns school
        every { clubRepository.findByNameAndSchool(request.clubName, school) } returns club
        every { securityUtil.passwordEncode(any()) } returns encodedPassword
        every { governmentRepository.save(any()) } returns government

        When("유관 기관 회원가입 요청을 하면") {
            authServiceImpl.governmentSignUp(request)

            Then("Government 가 저장이 되어야 한다.") {
                verify(exactly = 0) { userRepository.save(any()) }
                verify(exactly = 1) { governmentRepository.save(any()) }
            }
        }

        When("이미 존재하는 이메일로 유관 기관 회원가입 요청을 하면") {
            every { userRepository.existsByEmail(request.email) } returns true

            Then("AlreadyExistEmailException 가 터져야 한다.") {
                shouldThrow<AlreadyExistEmailException> {
                    authServiceImpl.governmentSignUp(request)
                }
            }
        }

        When("이미 존재하는 전화번호로 유관 기관 회원가입 요청을 하면") {
            every { userRepository.existsByPhoneNumber(request.phoneNumber) } returns true

            Then("AlreadyExistPhoneNumberException 가 터져야 한다.") {
                shouldThrow<AlreadyExistPhoneNumberException> {
                    authServiceImpl.governmentSignUp(request)
                }
            }
        }

        When("존재하지 않는 학교로 유관 기관 회원가입 요청을 하면") {
            every { schoolRepository.findByHighSchool(request.highSchool) } returns null

            Then("SchoolNotFoundException 가 터져야 한다.") {
                shouldThrow<SchoolNotFoundException> {
                    authServiceImpl.governmentSignUp(request)
                }
            }
        }

        When("존재하지 않는 동아리와 학교로 유관 기관 회원가입 요청을 하면") {
            every { clubRepository.findByNameAndSchool(request.clubName, school) } returns null

            Then("ClubNotFoundException 가 터져야 한다.") {
                shouldThrow<ClubNotFoundException> {
                    authServiceImpl.governmentSignUp(request)
                }
            }
        }
    }

    // companyInstructorSignUp 테스트 코드
    Given("CompanyInstructorSignUpRequest 가 주어지면") {
        val encodedPassword = "123456789a@"

        val request = fixture<CompanyInstructorSignUpRequest>()
        val school = fixture<School>()
        val club = fixture<Club>()
        val companyInstructor = fixture<CompanyInstructor>()

        every { userRepository.existsByEmail(request.email) } returns false
        every { userRepository.existsByPhoneNumber(request.phoneNumber) } returns false
        every { schoolRepository.findByHighSchool(request.highSchool) } returns school
        every { clubRepository.findByNameAndSchool(request.clubName, school) } returns club
        every { securityUtil.passwordEncode(any()) } returns encodedPassword
        every { companyInstructorRepository.save(any()) } returns companyInstructor

        When("기업 강사가 회원가입 요청을 하면") {
            authServiceImpl.companyInstructorSignUp(request)

            Then("CompanyInstructor 가 저장이 되어야 한다.") {
                verify(exactly = 0) { userRepository.save(any()) }
                verify(exactly = 1) { companyInstructorRepository.save(any()) }
            }
        }

        When("이미 존재하는 이메일로 기업 강사 회원가입 요청을 하면") {
            every { userRepository.existsByEmail(request.email) } returns true

            Then("AlreadyExistEmailException 가 터져야 한다.") {
                shouldThrow<AlreadyExistEmailException> {
                    authServiceImpl.companyInstructorSignUp(request)
                }
            }
        }

        When("이미 존재하는 전화번호로 기업 강사 회원가입 요청을 하면") {
            every { userRepository.existsByPhoneNumber(request.phoneNumber) } returns true

            Then("AlreadyExistPhoneNumberException 가 터져야 한다.") {
                shouldThrow<AlreadyExistPhoneNumberException> {
                    authServiceImpl.companyInstructorSignUp(request)
                }
            }
        }

        When("존재하지 않는 학교로 기업 강사 회원가입 요청을 하면") {
            every { schoolRepository.findByHighSchool(request.highSchool) } returns null

            Then("SchoolNotFoundException 가 터져야 한다.") {
                shouldThrow<SchoolNotFoundException> {
                    authServiceImpl.companyInstructorSignUp(request)
                }
            }
        }

        When("존재하지 않는 동아리와 학교로 기업 강 회원가입 요청을 하면") {
            every { clubRepository.findByNameAndSchool(request.clubName, school) } returns null

            Then("ClubNotFoundException 가 터져야 한다.") {
                shouldThrow<ClubNotFoundException> {
                    authServiceImpl.companyInstructorSignUp(request)
                }
            }
        }
    }

    // login 테스트 코드
    Given("LoginRequest 가 주어지면") {
        val password = "123456789a@"

        val request = fixture<LoginRequest> {
            property(LoginRequest::password) { password }
        }
        val response = fixture<TokenResponse>()
        val user = fixture<User> {
            property(User::approveStatus) { ApproveStatus.APPROVED }
        }
        val pendingUser = fixture<User> {
            property(User::approveStatus) { ApproveStatus.PENDING }
        }

        every { userRepository.findByEmail(request.email) } returns user
        every { securityUtil.isPasswordMatch(any(), any()) } returns true
        every { jwtTokenGenerator.generateToken(user.id, user.authority) } returns response

        When("로그인 요청을 하면") {
            val result = authServiceImpl.login(request)

            Then("result와 response가 같아야 한다.") {
                result shouldBe response
            }
        }

        When("이메일에 맞는 유저가 존재하지 않을 때") {
            every { userRepository.findByEmail(any()) } returns null

            Then("UserNotFoundException 이 터져야 한다.") {
                shouldThrow<UserNotFoundException> {
                    authServiceImpl.login(request)
                }
            }
        }

        When("비밀번호가 일치하지 않으면") {
            every { securityUtil.isPasswordMatch(any(), any()) } returns false

            Then("MisMatchPasswordException 이 터져야 한다.") {
                shouldThrow<MisMatchPasswordException> {
                    authServiceImpl.login(request)
                }
            }
        }

        When("ApproveStatus가 PENDING이라면") {
            every { userRepository.findByEmail(request.email) } returns pendingUser

            Then("UnApprovedUserException 이 터져야 한다.") {
                shouldThrow<UnApprovedUserException> {
                    authServiceImpl.login(request)
                }
            }
        }
    }

    // reissueToken 테스트 코드
    Given("requestToken 이 주어지면") {
        val request = "refreshToken"
        val refreshToken = "refreshToken"
        val token = fixture<RefreshToken> {
            property(RefreshToken::token) { request }
        }
        val user = fixture<User>()
        val response = fixture<TokenResponse>()

        every { jwtTokenParser.parseRefreshToken(request) } returns refreshToken
        every { refreshTokenRepository.findByIdOrNull(refreshToken) } returns token
        every { userRepository.findByIdOrNull(token.userId) } returns user
        every { refreshTokenRepository.deleteById(refreshToken) } returns Unit
        every { jwtTokenGenerator.generateToken(user.id, user.authority) } returns response

        When("토큰 재발급을 요청하면") {
            val result = authServiceImpl.reissueToken(request)

            Then("result와 response가 같아야 한다.") {
                result shouldBe response
            }
        }

        When("유효하지 않는 리프레시 토큰으로 요청했을 때") {
            every { jwtTokenParser.parseRefreshToken(request) } returns null

            Then("InvalidRefreshTokenException 이 터져야 한다.") {
                shouldThrow<InvalidRefreshTokenException> {
                    authServiceImpl.reissueToken(request)
                }
            }
        }

        When("존재하지 않는 리프레시 토큰으로 요청했을 때") {
            every { refreshTokenRepository.findByIdOrNull(refreshToken) } returns null

            Then("RefreshTokenNotFoundException 이 터져야 한다.") {
                shouldThrow<RefreshTokenNotFoundException> {
                    authServiceImpl.reissueToken(request)
                }
            }
        }

        When("토큰의 유저가 존재하지 않을 때") {
            every { userRepository.findByIdOrNull(token.userId) } returns null

            Then("UserNotFoundException 이 터져야 한다.") {
                shouldThrow<UserNotFoundException> {
                    authServiceImpl.reissueToken(request)
                }
            }
        }
    }

    // logout 테스트 코드
    Given("requestToken 이 주어지면") {
        val request = "refreshToken"
        val refreshToken = "refreshToken"
        val token = fixture<RefreshToken> {
            property(RefreshToken::token) { request }
        }
        val user = fixture<User> {
            property(User::id) { token.userId }
        }
        val invalidUser = fixture<User>()

        every { userUtil.queryCurrentUser() } returns user
        every { jwtTokenParser.parseRefreshToken(request) } returns refreshToken
        every { refreshTokenRepository.findByIdOrNull(refreshToken) } returns token
        every { refreshTokenRepository.delete(token) } returns Unit

        When("로그아웃을 요청하면") {
            authServiceImpl.logout(request)

            Then("RefreshToken이 삭제되어야 한다.") {
                verify(exactly = 1) { refreshTokenRepository.delete(token) }
            }
        }

        When("유효하지 않는 리프레시 토큰으로 요청했을 때") {
            every { jwtTokenParser.parseRefreshToken(request) } returns null

            Then("InvalidRefreshTokenException 이 터져야 한다.") {
                shouldThrow<InvalidRefreshTokenException> {
                    authServiceImpl.logout(request)
                }
            }
        }

        When("존재하지 않는 리프레시 토큰으로 요청했을 때") {
            every { refreshTokenRepository.findByIdOrNull(refreshToken) } returns null

            Then("RefreshTokenNotFoundException 이 터져야 한다.") {
                shouldThrow<RefreshTokenNotFoundException> {
                    authServiceImpl.logout(request)
                }
            }
        }

        When("토큰의 유저가 존재하지 않을 때") {
            every { userUtil.queryCurrentUser() } returns invalidUser

            Then("UserNotFoundException 이 터져야 한다.") {
                shouldThrow<UserNotFoundException> {
                    authServiceImpl.logout(request)
                }
            }
        }
    }

    // withdraw 테스트 코드
    Given("현재 로그인한 User가 있다면") {
        val user = fixture<User>()

        every { userUtil.queryCurrentUser() } returns user
        every { applicationEventPublisher.publishEvent(WithdrawUserEvent(user)) } just Runs
        every { userRepository.delete(user) } returns Unit

        When("회원탈퇴를 요청하면") {
            authServiceImpl.withdraw()

            Then("User가 삭제되어야 한다.") {
                verify(exactly = 1) { userRepository.delete(user) }
            }
        }
    }
})
