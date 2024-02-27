package team.msg.domain.club.service

import com.appmattus.kotlinfixture.kotlinFixture
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.equality.shouldBeEqualToIgnoringFields
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.data.repository.findByIdOrNull
import team.msg.common.util.UserUtil
import team.msg.domain.bbozzak.repository.BbozzakRepository
import team.msg.domain.club.exception.ClubNotFoundException
import team.msg.domain.club.model.Club
import team.msg.domain.club.presentation.data.response.ClubDetailsResponse
import team.msg.domain.club.presentation.data.response.ClubResponse
import team.msg.domain.club.presentation.data.response.ClubsResponse
import team.msg.domain.club.repository.ClubRepository
import team.msg.domain.company.repository.CompanyInstructorRepository
import team.msg.domain.government.repository.GovernmentRepository
import team.msg.domain.professor.repository.ProfessorRepository
import team.msg.domain.school.enums.HighSchool
import team.msg.domain.school.exception.SchoolNotFoundException
import team.msg.domain.school.model.School
import team.msg.domain.school.repository.SchoolRepository
import team.msg.domain.student.exception.StudentNotFoundException
import team.msg.domain.student.model.Student
import team.msg.domain.student.presentation.data.response.StudentDetailsResponse
import team.msg.domain.student.presentation.data.response.StudentResponse
import team.msg.domain.student.repository.StudentRepository
import team.msg.domain.teacher.model.Teacher
import team.msg.domain.teacher.presentation.data.response.TeacherResponse
import team.msg.domain.teacher.repository.TeacherRepository
import team.msg.domain.user.enums.Authority
import team.msg.domain.user.model.User
import java.util.*

class ClubServiceImplTest : BehaviorSpec({

    isolationMode = IsolationMode.InstancePerLeaf
    val fixture = kotlinFixture()

    val clubRepository = mockk<ClubRepository>()
    val schoolRepository = mockk<SchoolRepository>()
    val studentRepository = mockk<StudentRepository>()
    val userUtil = mockk<UserUtil>()
    val teacherRepository = mockk<TeacherRepository>()
    val bbozzakRepository = mockk<BbozzakRepository>()
    val professorRepository = mockk<ProfessorRepository>()
    val companyInstructorRepository = mockk<CompanyInstructorRepository>()
    val governmentRepository = mockk<GovernmentRepository>()
    val clubServiceImpl = ClubServiceImpl(
        clubRepository,
        schoolRepository,
        studentRepository,
        userUtil, teacherRepository,
        bbozzakRepository,
        professorRepository,
        companyInstructorRepository,
        governmentRepository
    )

    // queryAllClubsService 테스트 코드
    Given("Club 이 주어질 때") {
        val clubId = 0L
        val clubName = "dev GSM"
        val schoolName = "광주소프트웨어마이스터고등학교"

        val school = fixture<School> {
            property(School::highSchool) { HighSchool.GWANGJU_SOFTWARE_MEISTER_HIGH_SCHOOL }
        }
        val highSchool = fixture<HighSchool>()
        val club = fixture<Club> {
            property(Club::id) { clubId }
            property(Club::name) { clubName }
            property(Club::school) { school }
        }
        val clubResponse = fixture<ClubResponse> {
            property(ClubResponse::id) { clubId }
            property(ClubResponse::name) { clubName }
            property(ClubResponse::schoolName) { schoolName }
        }
        val response = fixture<ClubsResponse> {
            property(ClubsResponse::clubs) { listOf(clubResponse) }
        }

        every { schoolRepository.findByHighSchool(highSchool) } returns school
        every { clubRepository.findAllBySchool(school) } returns listOf(club)

        When("동아리 전체 조회 요청을 하면") {
            val result = clubServiceImpl.queryAllClubs(highSchool)

            Then("result와 response가 같아야 한다.") {
                result shouldBe response
            }
        }

        When("존재하지 않는 학교로 요청하면") {
            every { schoolRepository.findByHighSchool(highSchool) } returns null

            Then("SchoolNotFoundException 발생해야 한다.") {
                shouldThrow<SchoolNotFoundException> {
                    clubServiceImpl.queryAllClubs(highSchool)
                }
            }
        }
    }

    // queryClubDetailsByIdService 테스트 코드
    Given("Club 이 주어질 때") {
        val request = 0L
        val clubId = 0L
        val clubName = "dev GSM"
        val schoolName = "광주소프트웨어마이스터고등학교"
        val headCount = 1
        val studentId = UUID.randomUUID()
        val studentName = "박주홍"
        val teacherId = UUID.randomUUID()
        val teacherName = "김주홍"

        val school = fixture<School> {
            property(School::highSchool) { HighSchool.GWANGJU_SOFTWARE_MEISTER_HIGH_SCHOOL }
        }
        val studentUser = fixture<User> {
            property(User::name) { studentName }
        }
        val teacherUser = fixture<User> {
            property(User::name) { teacherName }
        }
        val student = fixture<Student> {
            property(Student::id) { studentId }
            property(Student::user) { studentUser }
        }
        val teacher = fixture<Teacher> {
            property(Teacher::id) { teacherId }
            property(Teacher::user) { teacherUser }
        }
        val club = fixture<Club> {
            property(Club::id) { clubId }
            property(Club::name) { clubName }
            property(Club::school) { school }
        }
        val studentResponse = fixture<StudentResponse> {
            property(StudentResponse::id) { student.id }
            property(StudentResponse::name) { student.user!!.name }
        }
        val teacherResponse = fixture<TeacherResponse> {
            property(TeacherResponse::id) { teacher.id }
            property(TeacherResponse::name) { teacher.user!!.name }
        }
        val response = fixture<ClubDetailsResponse> {
            property(ClubDetailsResponse::clubId) { clubId }
            property(ClubDetailsResponse::clubName) { clubName }
            property(ClubDetailsResponse::highSchoolName) { schoolName }
            property(ClubDetailsResponse::headCount) { headCount }
            property(ClubDetailsResponse::students) { listOf(studentResponse) }
            property(ClubDetailsResponse::teacher) { teacherResponse }
        }

        every { clubRepository.findByIdOrNull(request) } returns club
        every { studentRepository.findAllByClub(club) } returns listOf(student)
        every { teacherRepository.findByClub(club) } returns teacher

        When("동아리 상세 조회 요청을 하면") {
            val result = clubServiceImpl.queryClubDetailsById(request)

            Then("result와 response가 같아야 한다.") {
                result.students[0].id shouldBe response.students[0].id
                result.students[0].name shouldBe response.students[0].name
                result.shouldBeEqualToIgnoringFields(response, ClubDetailsResponse::students)
            }
        }

        When("존재하지 않는 동아리 id로 요청하면") {
            every { clubRepository.findByIdOrNull(request) } returns null

            Then("ClubNotFoundException 이 발생해야 한다.") {
                shouldThrow<ClubNotFoundException> {
                    clubServiceImpl.queryClubDetailsById(request)
                }
            }
        }
    }

    // queryMyClubDetailsService 테스트 코드
    Given("Club 이 주어질 때") {
        val clubId = 0L
        val clubName = "dev GSM"
        val schoolName = "광주소프트웨어마이스터고등학교"
        val headCount = 1
        val studentId = UUID.randomUUID()
        val teacherId = UUID.randomUUID()

        val studentUser = fixture<User> {
            property(User::authority) { Authority.ROLE_STUDENT }
        }
        val teacherUser = fixture<User> {
            property(User::authority) { Authority.ROLE_TEACHER }
        }
        val school = fixture<School> {
            property(School::highSchool) { HighSchool.GWANGJU_SOFTWARE_MEISTER_HIGH_SCHOOL }
        }
        val club = fixture<Club> {
            property(Club::id) { clubId }
            property(Club::name) { clubName }
            property(Club::school) { school }
        }
        val student = fixture<Student> {
            property(Student::id) { studentId }
            property(Student::user) { studentUser }
            property(Student::club) { club }
        }
        val teacher = fixture<Teacher> {
            property(Teacher::id) { teacherId }
            property(Teacher::user) { teacherUser }
            property(Teacher::club) { club }
        }

        val studentResponse = fixture<StudentResponse> {
            property(StudentResponse::id) { student.id }
            property(StudentResponse::name) { student.user!!.name }
        }
        val teacherResponse = fixture<TeacherResponse> {
            property(TeacherResponse::id) { teacher.id }
            property(TeacherResponse::name) { teacher.user!!.name }
        }
        val response = fixture<ClubDetailsResponse> {
            property(ClubDetailsResponse::clubId) { clubId }
            property(ClubDetailsResponse::clubName) { clubName }
            property(ClubDetailsResponse::highSchoolName) { schoolName }
            property(ClubDetailsResponse::headCount) { headCount }
            property(ClubDetailsResponse::students) { listOf(studentResponse) }
            property(ClubDetailsResponse::teacher) { teacherResponse }
        }

        every { userUtil.queryCurrentUser() } returns studentUser
        every { userUtil.getAuthorityEntityAndOrganization(studentUser).first } returns student
        every { studentRepository.findByUser(any()) } returns student
        every { studentRepository.findAllByClub(student.club) } returns listOf(student)
        every { teacherRepository.findByClub(student.club) } returns teacher

        When("동아리 상세 조회 요청을 하면") {
            val result = clubServiceImpl.queryMyClubDetails()

            Then("result와 response가 같아야 한다.") {
                result.students[0].id shouldBe response.students[0].id
                result.students[0].name shouldBe response.students[0].name
                result.shouldBeEqualToIgnoringFields(response, ClubDetailsResponse::students)
            }
        }
    }

    // queryStudentDetails 테스트 코드
    Given("clubId와 studentId 가 주어질 때") {
        val clubId = 0L
        val studentId = UUID.randomUUID()

        val studentUser = fixture<User> {
            property(User::authority) { Authority.ROLE_STUDENT }
        }
        val club = fixture<Club> {
            property(Club::id) { clubId }
        }
        val student = fixture<Student> {
            property(Student::id) { studentId }
            property(Student::user) { studentUser }
            property(Student::club) { club }
        }

        val response = fixture<StudentDetailsResponse> {
            property(StudentDetailsResponse::name) { studentUser.name }
            property(StudentDetailsResponse::phoneNumber) { studentUser.phoneNumber }
            property(StudentDetailsResponse::email) { studentUser.email }
            property(StudentDetailsResponse::credit) { student.credit }
        }

        every { clubRepository.findByIdOrNull(clubId) } returns club
        every { studentRepository.findByIdAndClub(studentId, club) } returns student

        When("동아리 소속 학생 상세정보 조회 요청을 하면") {
            val result = clubServiceImpl.queryStudentDetails(clubId, studentId)

            Then("result와 response가 같아야 한다.") {
                result shouldBe response
            }
        }

        When("존재하지 않는 동아리 id로 조회 요청을 하면") {
            every { clubRepository.findByIdOrNull(clubId) } returns null

            Then("ClubNotFoundException 가 발생해야 한다.") {
                shouldThrow<ClubNotFoundException> {
                    clubServiceImpl.queryStudentDetails(clubId, studentId)
                }
            }
        }

        When("존재하지 않는 학생 id로 조회 요청을 하면") {
            every { studentRepository.findByIdAndClub(studentId, club) } returns null

            Then("StudentNotFoundException 가 발생해야 한다.") {
                shouldThrow<StudentNotFoundException> {
                    clubServiceImpl.queryStudentDetails(clubId, studentId)
                }
            }
        }
    }

})