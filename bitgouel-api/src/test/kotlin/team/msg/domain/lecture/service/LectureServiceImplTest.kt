package team.msg.domain.lecture.service

import com.appmattus.kotlinfixture.kotlinFixture
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import team.msg.common.util.UserUtil
import team.msg.domain.bbozzak.model.Bbozzak
import team.msg.domain.bbozzak.repository.BbozzakRepository
import team.msg.domain.club.model.Club
import team.msg.domain.company.model.CompanyInstructor
import team.msg.domain.government.model.Government
import team.msg.domain.lecture.enums.LectureStatus
import team.msg.domain.lecture.enums.Semester
import team.msg.domain.lecture.exception.AlreadySignedUpLectureException
import team.msg.domain.lecture.exception.ForbiddenSignedUpLectureException
import team.msg.domain.lecture.exception.NotAvailableSignUpDateException
import team.msg.domain.lecture.exception.OverMaxRegisteredUserException
import team.msg.domain.lecture.exception.UnSignedUpLectureException
import team.msg.domain.lecture.model.Lecture
import team.msg.domain.lecture.model.LectureDate
import team.msg.domain.lecture.model.RegisteredLecture
import team.msg.domain.lecture.presentation.data.request.CreateLectureRequest
import team.msg.domain.lecture.presentation.data.request.QueryAllDepartmentsRequest
import team.msg.domain.lecture.presentation.data.request.QueryAllDivisionsRequest
import team.msg.domain.lecture.presentation.data.request.QueryAllLectureRequest
import team.msg.domain.lecture.presentation.data.request.QueryAllLinesRequest
import team.msg.domain.lecture.presentation.data.response.*
import team.msg.domain.lecture.repository.LectureDateRepository
import team.msg.domain.lecture.repository.LectureRepository
import team.msg.domain.lecture.repository.RegisteredLectureRepository
import team.msg.domain.professor.model.Professor
import team.msg.domain.professor.repository.ProfessorRepository
import team.msg.domain.school.enums.HighSchool
import team.msg.domain.school.model.School
import team.msg.domain.student.exception.StudentNotFoundException
import team.msg.domain.student.model.Student
import team.msg.domain.student.repository.StudentRepository
import team.msg.domain.teacher.model.Teacher
import team.msg.domain.teacher.repository.TeacherRepository
import team.msg.domain.user.enums.Authority
import team.msg.domain.user.exception.UserNotFoundException
import team.msg.domain.user.model.User
import team.msg.domain.user.repository.UserRepository
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

class LectureServiceImplTest : BehaviorSpec({

    isolationMode = IsolationMode.InstancePerLeaf
    val fixture = kotlinFixture()

    val lectureRepository = mockk<LectureRepository>()
    val lectureDateRepository = mockk<LectureDateRepository>()
    val registeredLectureRepository = mockk<RegisteredLectureRepository>()
    val studentRepository = mockk<StudentRepository>()
    val teacherRepository = mockk<TeacherRepository>()
    val professorRepository = mockk<ProfessorRepository>()
    val userRepository = mockk<UserRepository>()
    val bbozzakRepository = mockk<BbozzakRepository>()
    val userUtil = mockk<UserUtil>()
    val pageable = mockk<Pageable>()
    val lectureServiceImpl = LectureServiceImpl(
        lectureRepository,
        lectureDateRepository,
        registeredLectureRepository,
        studentRepository,
        teacherRepository,
        bbozzakRepository,
        professorRepository,
        userRepository,
        userUtil
    )

    // createLecture 테스트 코드
    Given("CreateLectureRequest 가 주어질 때") {

        val user = fixture<User>()
        val request = fixture<CreateLectureRequest>()
        val lecture = fixture<Lecture>()
        val lectureDate = fixture<LectureDate>()
        val lectureDates = mutableListOf(lectureDate)

        every { userRepository.findByIdOrNull(any()) } returns user
        every { lectureRepository.save(any()) } returns lecture
        every { lectureDateRepository.saveAll(any<List<LectureDate>>()) } returns lectureDates

        When("Lecture 등록 요청을 하면") {
            lectureServiceImpl.createLecture(request)

            Then("Lecture 가 저장이 되어야 한다.") {
                verify(exactly = 1) { lectureRepository.save(any()) }
            }

            Then("LectureDate 가 저장이 되어야 한다.") {
                verify(exactly = 1) { lectureDateRepository.saveAll(any<List<LectureDate>>()) }
            }
        }

        When("존재하지 않는 유저 id로 요청을 하면") {
            every { userRepository.findByIdOrNull(any()) } returns null

            Then("UserNotFoundException 예외가 발생해야 한다.") {
                shouldThrow<UserNotFoundException> {
                    lectureServiceImpl.createLecture(request)
                }
            }

        }
    }

    //queryAllLectures 테스트 코드
    Given("queryAllLectureRequest와 Pageable가 주어질 때") {

        val queryAllLectureRequest = fixture<QueryAllLectureRequest>()

        val name = "name"
        val content = "content"
        val instructor = "instructor"
        val headCount = 0
        val maxRegisteredUser = 5
        val startDate = LocalDateTime.MIN
        val endDate = LocalDateTime.MAX
        val completeDate = LocalDateTime.MAX
        val lectureStatus = LectureStatus.OPENED
        val semester = Semester.FIRST_YEAR_FALL_SEMESTER
        val division = "division"
        val line = "line"
        val department = "department"

        val creditLectureId = UUID.randomUUID()
        val creditLectureType = "상호학점인정교육과정"

        val universityLectureId = UUID.randomUUID()
        val universityLectureType = "대학탐방프로그램"

        val creditLecture = fixture<Lecture> {
            property(Lecture::id) { creditLectureId }
            property(Lecture::name) { name }
            property(Lecture::content) { content }
            property(Lecture::lectureType) { creditLectureType }
            property(Lecture::maxRegisteredUser) { maxRegisteredUser }
            property(Lecture::startDate) { startDate }
            property(Lecture::endDate) { endDate }
            property(Lecture::instructor) { instructor }
            property(Lecture::semester) { semester }
            property(Lecture::division) { division }
            property(Lecture::line) { line }
            property(Lecture::department) { department }
        }
        val universityLecture = fixture<Lecture> {
            property(Lecture::id) { universityLectureId }
            property(Lecture::name) { name }
            property(Lecture::content) { content }
            property(Lecture::lectureType) { universityLectureType }
            property(Lecture::maxRegisteredUser) { maxRegisteredUser }
            property(Lecture::startDate) { startDate }
            property(Lecture::endDate) { endDate }
            property(Lecture::instructor) { instructor }
            property(Lecture::semester) { semester }
            property(Lecture::division) { division }
            property(Lecture::line) { line }
            property(Lecture::department) { department }
        }

        val creditLectureResponse = fixture<LectureResponse> {
            property(LectureResponse::id) { creditLectureId }
            property(LectureResponse::name) { name }
            property(LectureResponse::content) { content }
            property(LectureResponse::lectureType) { creditLectureType }
            property(LectureResponse::headCount) { headCount }
            property(LectureResponse::maxRegisteredUser) { maxRegisteredUser }
            property(LectureResponse::startDate) { startDate }
            property(LectureResponse::endDate) { endDate }
            property(LectureResponse::lecturer) { instructor }
            property(LectureResponse::lectureStatus) { lectureStatus }
            property(LectureResponse::semester) { semester }
            property(LectureResponse::division) { division }
            property(LectureResponse::line) { line }
            property(LectureResponse::department) { department }
        }
        val universityLectureResponse = fixture<LectureResponse> {
            property(LectureResponse::id) { universityLectureId }
            property(LectureResponse::name) { name }
            property(LectureResponse::content) { content }
            property(LectureResponse::lectureType) { universityLectureType }
            property(LectureResponse::headCount) { headCount }
            property(LectureResponse::maxRegisteredUser) { maxRegisteredUser }
            property(LectureResponse::startDate) { startDate }
            property(LectureResponse::endDate) { endDate }
            property(LectureResponse::lecturer) { instructor }
            property(LectureResponse::lectureStatus) { lectureStatus }
            property(LectureResponse::semester) { semester }
            property(LectureResponse::division) { division }
            property(LectureResponse::line) { line }
            property(LectureResponse::department) { department }
        }

        every { registeredLectureRepository.countByLecture(any()) } returns headCount

        When("주어진 LectureType이 null이라면") {
            every { lectureRepository.findAllByLectureType(any(), any()) } returns PageImpl(listOf(creditLecture, universityLecture))

            val response = fixture<LecturesResponse> {
                property(LecturesResponse::lectures) { PageImpl(listOf(creditLectureResponse, universityLectureResponse)) }
            }

            val result = lectureServiceImpl.queryAllLectures(pageable, queryAllLectureRequest)

            Then("result와 response가 같아야 한다") {
                result shouldBe response
            }
        }

        When("주어진 LectureType이 상호학점인정과정이라면") {
            every { lectureRepository.findAllByLectureType(any(), any()) } returns PageImpl(listOf(creditLecture))

            val response = fixture<LecturesResponse> {
                property(LecturesResponse::lectures) { PageImpl(listOf(creditLectureResponse)) }
            }

            val result = lectureServiceImpl.queryAllLectures(pageable, queryAllLectureRequest)

            Then("result와 response가 같아야 한다") {
                result shouldBe response
            }
        }

        When("주어진 LectureType이 대학탐방프로그램이라면") {
            every { lectureRepository.findAllByLectureType(any(), any()) } returns PageImpl(listOf(universityLecture))

            val response = fixture<LecturesResponse> {
                property(LecturesResponse::lectures) { PageImpl(listOf(universityLectureResponse)) }
            }

            val result = lectureServiceImpl.queryAllLectures(pageable, queryAllLectureRequest)

            Then("result와 response가 같아야 한다") {
                result shouldBe response
            }
        }
    }

    // queryLectureDetails 테스트 코드
    Given("Lecture id가 주어졌을 때") {

        val userId = UUID.randomUUID()
        val studentAuthority = Authority.ROLE_STUDENT
        val user = fixture<User> {
            property(User::id) { userId }
            property(User::authority) { studentAuthority }
        }

        val studentId = UUID.randomUUID()
        val student = fixture<Student> {
            property(Student::id) { studentId }
            property(Student::user) { user }
        }

        val lectureId = UUID.randomUUID()
        val name = "name"
        val content = "content"
        val instructor = "instructor"
        val headCount = 0
        val maxRegisteredUser = 5
        val credit = 2
        val startDate = LocalDateTime.MIN
        val endDate = LocalDateTime.MAX
        val lectureStatus = LectureStatus.OPENED
        val lectureType = "상호학점인정교육과정"
        val isRegistered = false
        val semester = Semester.FIRST_YEAR_FALL_SEMESTER
        val division = "자동차 산업"
        val line = "line"
        val department = "department"
        val completeDate = LocalDate.MAX
        val startTime = LocalTime.MIN
        val endTime = LocalTime.MAX

        val lectureDate = fixture<LectureDate> {
            property(LectureDate::completeDate) { completeDate }
            property(LectureDate::startTime) { startTime }
            property(LectureDate::endTime) { endTime }
        }
        val lectureDates = mutableListOf(lectureDate)

        val lecture = fixture<Lecture> {
            property(Lecture::id) { lectureId }
            property(Lecture::name) { name }
            property(Lecture::content) { content }
            property(Lecture::lectureType) { lectureType }
            property(Lecture::maxRegisteredUser) { maxRegisteredUser }
            property(Lecture::startDate) { startDate }
            property(Lecture::endDate) { endDate }
            property(Lecture::instructor) { instructor }
            property(Lecture::credit) { credit }
            property(Lecture::semester) { semester }
            property(Lecture::division) { division }
            property(Lecture::line) { line }
            property(Lecture::department) { department }
        }

        val lectureDateResponse = fixture<LectureDateResponse> {
            property(LectureDateResponse::completeDate) { completeDate }
            property(LectureDateResponse::startTime) { startTime }
            property(LectureDateResponse::endTime) { endTime }
        }

        val lectureDateResponses = mutableListOf(lectureDateResponse)

        val response = fixture<LectureDetailsResponse> {
            property(LectureDetailsResponse::name) { name }
            property(LectureDetailsResponse::content) { content }
            property(LectureDetailsResponse::lectureType) { lectureType }
            property(LectureDetailsResponse::headCount) { headCount }
            property(LectureDetailsResponse::maxRegisteredUser) { maxRegisteredUser }
            property(LectureDetailsResponse::startDate) { startDate }
            property(LectureDetailsResponse::endDate) { endDate }
            property(LectureDetailsResponse::lectureDates) { lectureDateResponses }
            property(LectureDetailsResponse::lecturer) { instructor }
            property(LectureDetailsResponse::lectureStatus) { lectureStatus }
            property(LectureDetailsResponse::isRegistered) { isRegistered }
            property(LectureDetailsResponse::createAt) { lecture.createdAt }
            property(LectureDetailsResponse::credit) { credit }
            property(LectureDetailsResponse::semester) { semester }
            property(LectureDetailsResponse::division) { division }
            property(LectureDetailsResponse::line) { line }
            property(LectureDetailsResponse::department) { department }
        }

        every { userUtil.queryCurrentUser() } returns user
        every { lectureRepository.findByIdOrNull(lectureId) } returns lecture
        every { lectureDateRepository.findAllByLecture(lecture) } returns lectureDates
        every { registeredLectureRepository.countByLecture(any()) } returns headCount
        every { studentRepository.findByUser(any()) } returns student
        every { registeredLectureRepository.existsOne(any(), any()) } returns isRegistered

        When("강의 상세 정보를 조회하면") {
            val result = lectureServiceImpl.queryLectureDetails(lectureId)

            Then("result와 response가 같아야 한다") {
                result shouldBe response
            }
        }

        When("조회한 학생이 신청한 강의라면") {
            every { registeredLectureRepository.existsOne(any(), any()) } returns true

            val result = lectureServiceImpl.queryLectureDetails(lectureId)

            Then("isResistered가 true여야 한다."){
                result.isRegistered shouldBe true
            }
        }
    }

    //signUpLecture 테스트 코드
    Given("Lecture id가 주어질 때") {

        val userId = UUID.randomUUID()
        val studentAuthority = Authority.ROLE_STUDENT
        val user = fixture<User> {
            property(User::id) { userId }
            property(User::authority) { studentAuthority }
        }

        val studentId = UUID.randomUUID()
        val student = fixture<Student> {
            property(Student::id) { studentId }
            property(Student::user) { user }
        }

        val lectureId = UUID.randomUUID()
        val name = "name"
        val content = "content"
        val instructor = "instructor"
        val maxRegisteredUser = 5
        val credit = 2
        val headCount = 0
        val startDate = LocalDateTime.MIN
        val endDate = LocalDateTime.MAX
        val lectureType = "상호학점인정교육과정"
        val lectureDate = fixture<LectureDate>()

        val lecture = fixture<Lecture> {
            property(Lecture::id) { lectureId }
            property(Lecture::name) { name }
            property(Lecture::content) { content }
            property(Lecture::lectureType) { lectureType }
            property(Lecture::maxRegisteredUser) { maxRegisteredUser }
            property(Lecture::startDate) { startDate }
            property(Lecture::endDate) { endDate }
            property(Lecture::instructor) { instructor }
            property(Lecture::credit) { credit }
        }

        val missDateLectureId = UUID.randomUUID()
        val missEndDate = LocalDateTime.MIN

        val missDateLecture = fixture<Lecture> {
            property(Lecture::id) { missDateLectureId }
            property(Lecture::name) { name }
            property(Lecture::content) { content }
            property(Lecture::lectureType) { lectureType }
            property(Lecture::maxRegisteredUser) { maxRegisteredUser }
            property(Lecture::startDate) { startDate }
            property(Lecture::endDate) { missEndDate }
            property(Lecture::instructor) { instructor }
            property(Lecture::credit) { credit }
        }

        val registeredLecture = fixture<RegisteredLecture>()

        every { userUtil.queryCurrentUser() } returns user
        every { studentRepository.findByUser(any()) } returns student
        every { lectureRepository.findByIdOrNull(lectureId) } returns lecture
        every { registeredLectureRepository.existsOne(any(), any()) } returns false
        every { registeredLectureRepository.countByLecture(any()) } returns headCount
        every { registeredLectureRepository.save(any()) } returns registeredLecture
        every { studentRepository.save(any()) } returns student


        When("학생이 강의 수강 신청을 하면") {
            lectureServiceImpl.signUpLecture(lectureId)

            Then("RegisteredLecture 가 저장되어야 한다.") {
                verify(exactly = 1) { registeredLectureRepository.save(any()) }
            }

            Then("Student가 저장되어야 한다.") {
                verify(exactly = 1) { studentRepository.save(any()) }
            }
        }

        When("현재 유저가 학생이 아니라면") {
            every { studentRepository.findByUser(user) } returns null

            Then("StudentNotFoundException이 발생해야 한다.") {
                shouldThrow<StudentNotFoundException> {
                    lectureServiceImpl.signUpLecture(lectureId)
                }
            }
        }

        When("수강 신청 시간이 아닌 강의에 수강 신청을 하면") {
            every { lectureRepository.findByIdOrNull(missDateLectureId) } returns missDateLecture

            Then("NotAvailableSignUpDateException이 발생해야 한다.") {
                shouldThrow<NotAvailableSignUpDateException> {
                    lectureServiceImpl.signUpLecture(missDateLectureId)
                }
            }
        }

        When("이미 수강 신청한 강의에 수강 신청을 하면") {
            every { registeredLectureRepository.existsOne(any(), any()) } returns true

            Then("AlreadySignedUpLectureException이 발생해야 한다.") {
                shouldThrow<AlreadySignedUpLectureException> {
                    lectureServiceImpl.signUpLecture(lectureId)
                }
            }
        }

        When("수강 인원이 가득 찬 강의에 수강 신청을 하면") {
            every { registeredLectureRepository.countByLecture(any()) } returns maxRegisteredUser

            Then("OverMaxRegisteredUserException이 발생해야 한다.") {
                shouldThrow<OverMaxRegisteredUserException> {
                    lectureServiceImpl.signUpLecture(lectureId)
                }
            }
        }
    }

    //cancelSignUpLecture 테스트 코드
    Given("Lecture id가 주어질 때") {

        val userId = UUID.randomUUID()
        val studentAuthority = Authority.ROLE_STUDENT
        val user = fixture<User> {
            property(User::id) { userId }
            property(User::authority) { studentAuthority }
        }

        val studentId = UUID.randomUUID()
        val student = fixture<Student> {
            property(Student::id) { studentId }
            property(Student::user) { user }
        }

        val lectureId = UUID.randomUUID()
        val name = "name"
        val content = "content"
        val instructor = "instructor"
        val maxRegisteredUser = 5
        val credit = 2
        val startDate = LocalDateTime.MIN
        val endDate = LocalDateTime.MAX
        val lectureType = "상호학점인정교육과정"

        val lecture = fixture<Lecture> {
            property(Lecture::id) { lectureId }
            property(Lecture::name) { name }
            property(Lecture::content) { content }
            property(Lecture::lectureType) { lectureType }
            property(Lecture::maxRegisteredUser) { maxRegisteredUser }
            property(Lecture::startDate) { startDate }
            property(Lecture::endDate) { endDate }
            property(Lecture::instructor) { instructor }
            property(Lecture::credit) { credit }
        }

        val missDateLectureId = UUID.randomUUID()
        val missEndDate = LocalDateTime.MIN

        val missDateLecture = fixture<Lecture> {
            property(Lecture::id) { missDateLectureId }
            property(Lecture::name) { name }
            property(Lecture::content) { content }
            property(Lecture::lectureType) { lectureType }
            property(Lecture::maxRegisteredUser) { maxRegisteredUser }
            property(Lecture::startDate) { startDate }
            property(Lecture::endDate) { missEndDate }
            property(Lecture::instructor) { instructor }
            property(Lecture::credit) { credit }
        }

        val registeredLecture = fixture<RegisteredLecture>()

        every { userUtil.queryCurrentUser() } returns user
        every { studentRepository.findByUser(any()) } returns student
        every { lectureRepository.findByIdOrNull(lectureId) } returns lecture
        every { registeredLectureRepository.findByStudentAndLecture(any(), any()) } returns registeredLecture
        every { registeredLectureRepository.delete(any()) } returns Unit
        every { studentRepository.save(any()) } returns student


        When("학생이 강의 수강 신청을 취소하면") {
            lectureServiceImpl.cancelSignUpLecture(lectureId)

            Then("RegisteredLecture 가 삭제되어야 한다.") {
                verify(exactly = 1) { registeredLectureRepository.delete(any()) }
            }

            Then("Student가 저장되어야 한다.") {
                verify(exactly = 1) { studentRepository.save(any()) }
            }
        }

        When("현재 유저가 학생이 아니라면") {
            every { studentRepository.findByUser(user) } returns null

            Then("StudentNotFoundException이 발생해야 한다.") {
                shouldThrow<StudentNotFoundException> {
                    lectureServiceImpl.cancelSignUpLecture(lectureId)
                }
            }
        }

        When("수강 신청 시간이 아닌 강의에 수강 신청을 하면") {
            every { lectureRepository.findByIdOrNull(missDateLectureId) } returns missDateLecture

            Then("NotAvailableSignUpDateException이 발생해야 한다.") {
                shouldThrow<NotAvailableSignUpDateException> {
                    lectureServiceImpl.signUpLecture(missDateLectureId)
                }
            }
        }

        When("수강 신청을 하지 않았는데 수강 신청을 취소하면") {
            every { registeredLectureRepository.findByStudentAndLecture(any(), any()) } returns null

            Then("UnSignedUpLectureException이 발생해야 한다.") {
                shouldThrow<UnSignedUpLectureException> {
                    lectureServiceImpl.cancelSignUpLecture(lectureId)
                }
            }
        }
    }

    // queryInstructors 테스트 코드
    Given("강사와 keyword가 주어질 때"){
        val professorUserId = UUID.randomUUID()
        val professorUserName = "professor"
        val professorAuthority = Authority.ROLE_PROFESSOR
        val professorUser = fixture<User> {
            property(User::id) { professorUserId }
            property(User::name) { professorUserName }
            property(User::authority) { professorAuthority }
        }
        val university = "university"
        val professor = fixture<Professor> {
            property(Professor::user) { professorUser }
            property(Professor::university) { university }
        }
        val professorPair = Pair(professorUser, university)
        val professorResponse = LectureResponse.instructorOf(professorUser, university)

        val companyInstructorUserId = UUID.randomUUID()
        val companyInstructorUserName = "companyInstructor"
        val companyInstructorAuthority = Authority.ROLE_COMPANY_INSTRUCTOR
        val companyInstructorUser = fixture<User> {
            property(User::id) { companyInstructorUserId }
            property(User::name) { companyInstructorUserName }
            property(User::authority) { companyInstructorAuthority }
        }
        val company = "company"
        val companyInstructor = fixture<CompanyInstructor> {
            property(CompanyInstructor::user) { companyInstructorUser }
            property(CompanyInstructor::company) { company }
        }
        val companyInstructorPair = Pair(companyInstructorUser, company)
        val companyInstructorResponse = LectureResponse.instructorOf(companyInstructorUser, company)

        val governmentUserName = "government"
        val governmentAuthority = Authority.ROLE_GOVERNMENT
        val governmentUser = fixture<User> {
            property(User::name) { governmentUserName }
            property(User::authority) { governmentAuthority }
        }
        val governmentId = UUID.randomUUID()
        val governmentName = "governmentName"
        val government = fixture<Government> {
            property(Government::id) { governmentId }
            property(Government::user) { governmentUser }
            property(Government::governmentName) { governmentName }
        }
        val governmentPair = Pair(governmentUser, governmentName)
        val governmentResponse = LectureResponse.instructorOf(governmentUser, governmentName)

        When("keyword가 빈 문자열일 때") {
            every { userRepository.queryInstructorsAndOrganization(any()) } returns listOf(professorPair, companyInstructorPair, governmentPair)

            val response = InstructorsResponse(listOf(professorResponse, companyInstructorResponse, governmentResponse))

            val keyword = ""

            val result = lectureServiceImpl.queryInstructors(keyword)
            Then("result와 response가 같아야 한다") {
                result shouldBe response
            }
        }

        When("keyword가 특정 강사의 이름이나 기관에 포함되는 문자열일 때") {
            every { userRepository.queryInstructorsAndOrganization(any()) } returns listOf(professorPair, companyInstructorPair)

            val response = InstructorsResponse(listOf(professorResponse, companyInstructorResponse))

            val keyword = "y"

            val result = lectureServiceImpl.queryInstructors(keyword)
            Then("result와 response가 같아야 한다") {
                result shouldBe response
            }
        }

    }

    // queryAllLines 테스트 코드
    Given("강의와 Division, keyword가 주어질 때"){
        val emptyKeyword = ""
        val keyword = "기"
        val division = "자동차 산업"

        val request = fixture<QueryAllLinesRequest> {
            property(QueryAllLinesRequest::keyword) { keyword }
            property(QueryAllLinesRequest::division) { division }
        }
        val emptyKeywordRequest = fixture<QueryAllLinesRequest> {
            property(QueryAllLinesRequest::keyword) { emptyKeyword }
            property(QueryAllLinesRequest::division) { division }
        }

        val lines = mutableListOf("기계")
        val emptyKeywordLines = mutableListOf("기계", "자동차")

        val response = LectureResponse.lineOf(lines)
        val emptyKeywordResponse = LectureResponse.lineOf(emptyKeywordLines)

        every { lectureRepository.findAllLineByDivision(division, keyword) } returns lines
        every { lectureRepository.findAllLineByDivision(division, emptyKeyword) } returns emptyKeywordLines
        When("keyqord가 빈 문자열일 때") {
            val result = lectureServiceImpl.queryAllLines(emptyKeywordRequest)
            Then("result와 response가 같아야 한다") {
                result shouldBe emptyKeywordResponse
            }
        }

        When("keyword가 특정 계열에 포함되는 문자열일 때"){
            val result = lectureServiceImpl.queryAllLines(request)
            Then("result와 response가 같아야 한다") {
                result shouldBe response
            }
        }
    }

    // queryAllDepartments 테스트 코드
    Given("강의와 keyword가 주어질 때"){
        val emptyKeyword = ""
        val keyword = "자"

        val request = fixture<QueryAllDepartmentsRequest> {
            property(QueryAllDepartmentsRequest::keyword) { keyword }
        }
        val emptyKeywordRequest = fixture<QueryAllDepartmentsRequest> {
            property(QueryAllDepartmentsRequest::keyword) { emptyKeyword }
        }

        val departments = mutableListOf("자동차공학")
        val emptyKeywordDepartments = mutableListOf("자동차공학", "기계공학")

        val response = LectureResponse.departmentOf(departments)
        val emptyKeywordResponse = LectureResponse.departmentOf(emptyKeywordDepartments)

        When("keyqord가 빈 문자열일 때") {
            every { lectureRepository.findAllDepartment(any()) } returns emptyKeywordDepartments
            val result = lectureServiceImpl.queryAllDepartments(emptyKeywordRequest)
            Then("result와 response가 같아야 한다") {
                result shouldBe emptyKeywordResponse
            }
        }

        When("keyword가 특정 학과에 포함되는 문자열일 때"){
            every { lectureRepository.findAllDepartment(any()) } returns departments
            val result = lectureServiceImpl.queryAllDepartments(request)
            Then("result와 response가 같아야 한다") {
                result shouldBe response
            }
        }
    }

    // queryAllDivisions 테스트 코드
    Given("강의와 keyword가 주어질 때"){
        val emptyKeyword = ""
        val keyword = "상호"

        val request = fixture<QueryAllDivisionsRequest> {
            property(QueryAllDivisionsRequest::keyword) { keyword }
        }
        val emptyKeywordRequest = fixture<QueryAllDivisionsRequest> {
            property(QueryAllDivisionsRequest::keyword) { emptyKeyword }
        }

        val departments = mutableListOf("상호학점인정교육과정")
        val emptyKeywordDepartments = mutableListOf("상호학점인정교육과정", "대학탐방프로그램")

        val response = LectureResponse.divisionOf(departments)
        val emptyKeywordResponse = LectureResponse.divisionOf(emptyKeywordDepartments)

        When("keyqord가 빈 문자열일 때") {
            every { lectureRepository.findAllDivisions(any()) } returns emptyKeywordDepartments
            val result = lectureServiceImpl.queryAllDivisions(emptyKeywordRequest)
            Then("result와 response가 같아야 한다") {
                result shouldBe emptyKeywordResponse
            }
        }

        When("keyword가 특정 학과에 포함되는 문자열일 때"){
            every { lectureRepository.findAllDivisions(any()) } returns departments
            val result = lectureServiceImpl.queryAllDivisions(request)
            Then("result와 response가 같아야 한다") {
                result shouldBe response
            }
        }
    }

    // queryAllSignedUpLectures 테스트 코드
    Given("user와 student id가 주어질 때") {
        val userId = UUID.randomUUID()
        val teacherId = UUID.randomUUID()
        val studentId = UUID.randomUUID()

        val club1 = fixture<Club>()
        val club2 = fixture<Club>()

        val teacherUser = fixture<User> {
            property(User::id) { userId }
            property(User::authority) { Authority.ROLE_TEACHER }
        }
        val teacher = fixture<Teacher> {
            property(Teacher::id) { teacherId }
            property(Teacher::user) { teacherUser }
            property(Teacher::club) { club1 }
        }

        val adminUser = fixture<User> {
            property(User::id) { userId }
            property(User::authority) { Authority.ROLE_ADMIN }
        }

        val club1Student = fixture<Student> {
            property(Student::id) { studentId }
            property(Student::club) { club1 }
        }
        val club2Student = fixture<Student> {
            property(Student::id) { studentId }
            property(Student::club) { club2 }
        }

        val lectureId = UUID.randomUUID()
        val lectureName = "name"
        val lectureType = "type"
        val lecturer = "lecturer"

        val isComplete = false

        val lecture = fixture<Lecture> {
            property(Lecture::id) { lectureId }
            property(Lecture::name) { lectureName }
            property(Lecture::lectureType) { lectureType }
            property(Lecture::instructor) { lecturer }
        }

        val lectureDate1 = fixture<LectureDate> {
            property(LectureDate::completeDate) { LocalDate.MIN }
            property(LectureDate::lecture) { lecture }
        }
        val lectureDate2 = fixture<LectureDate> {
            property(LectureDate::completeDate) { LocalDate.MAX }
            property(LectureDate::lecture) { lecture }
        }

        val lectureAndIsComplete = listOf(Pair(lecture, isComplete))

        val lectureResponse = LectureResponse.of(lecture, isComplete, LocalDate.MAX)
        val response = LectureResponse.signedUpOf(listOf(lectureResponse))

        every { teacherRepository.findByUser(teacherUser) } returns teacher

        When("현재 로그인 한 유저가 Admin일 때") {
            every { userUtil.queryCurrentUser() } returns adminUser
            every { registeredLectureRepository.findLecturesAndIsCompleteByStudentId(studentId) } returns lectureAndIsComplete
            every { lectureDateRepository.findByCurrentCompletedDate(lectureId) } returns LocalDate.MAX

            val result = lectureServiceImpl.queryAllSignedUpLectures(studentId)
            Then("result와 response가 같아야 한다") {
                result shouldBe response
            }
        }

        When("현재 로그인 한 유저가 Teacher이고, 학생과 동아리가 다르면") {
            every { userUtil.queryCurrentUser() } returns teacherUser
            every { studentRepository.findByIdOrNull(any()) } returns club2Student

            Then("ForbiddenCompletedLectureException이 발생해야 한다.") {
                shouldThrow<ForbiddenSignedUpLectureException> {
                    lectureServiceImpl.queryAllSignedUpLectures(studentId)
                }
            }
        }
    }

    // queryAllSignedUpLectures 테스트 코드
    Given("lecture id가 주어질 때") {
        val studentUserId = UUID.randomUUID()
        val studentEmail = "email"
        val studentName = "name"
        val studentPhoneNumber = "phoneNumber"

        val studentUserA = fixture<User>{
            property(User::id) { studentUserId }
            property(User::name) { studentName }
            property(User::email) { studentEmail }
            property(User::phoneNumber) { studentPhoneNumber}
        }
        val studentUserB = fixture<User>{
            property(User::id) { studentUserId }
            property(User::name) { studentName }
            property(User::email) { studentEmail }
            property(User::phoneNumber) { studentPhoneNumber}
        }

        val highSchool = HighSchool.KUMPA_TECHNICAL_HIGH_SCHOOL

        val school = fixture<School> {
            property(School::highSchool) { highSchool }
        }
        val clubAId = 0L
        val clubBId = 1L
        val clubAName = "clubAName"
        val clubBName = "clubBName"

        val clubA = fixture<Club> {
            property(Club::id) { clubAId }
            property(Club::school) { school }
            property(Club::name) { clubAName }
        }
        val clubB = fixture<Club> {
            property(Club::id) { clubBId }
            property(Club::school) { school }
            property(Club::name) { clubBName }
        }

        val studentId = UUID.randomUUID()
        val grade = 1
        val classRoom = 2
        val number = 3
        val cohort = 4

        val studentA = fixture<Student> {
            property(Student::id) { studentId }
            property(Student::user) { studentUserA }
            property(Student::club) { clubA }
            property(Student::grade) { grade }
            property(Student::classRoom) { classRoom }
            property(Student::number) { number }
            property(Student::cohort) { cohort }
        }
        val studentB = fixture<Student> {
            property(Student::id) { studentId }
            property(Student::user) { studentUserB }
            property(Student::club) { clubB }
            property(Student::grade) { grade }
            property(Student::classRoom) { classRoom }
            property(Student::number) { number }
            property(Student::cohort) { cohort }
        }

        val students = listOf(studentA, studentB)
        val clubAStudents = listOf(studentA)
        val clubBStudents = listOf(studentB)

        val teacherUser = fixture<User>() {
            property(User::authority) { Authority.ROLE_TEACHER }

        }
        val teacher = fixture<Teacher> {
            property(Teacher::user) { teacherUser }
            property(Teacher::club) { clubA }
        }

        val bbozzakUser = fixture<User> {
            property(User::authority) { Authority.ROLE_BBOZZAK }
        }
        val bbozzak = fixture<Bbozzak> {
            property(Bbozzak::user) { bbozzakUser }
            property(Bbozzak::club) { clubB }
        }

        val professorUserAId = UUID.randomUUID()
        val professorUserBId = UUID.randomUUID()

        val professorUserA = fixture<User> {
            property(User::id) { professorUserAId}
            property(User::authority) { Authority.ROLE_PROFESSOR }
        }
        val professorUserB = fixture<User> {
            property(User::id) { professorUserBId}
            property(User::authority) { Authority.ROLE_PROFESSOR }
        }

        val adminUser = fixture<User> {
            property(User::authority) { Authority.ROLE_ADMIN }
        }

        val lectureId = UUID.randomUUID()

        val lecture = fixture<Lecture> {
            property(Lecture::id) { lectureId }
            property(Lecture::user) { professorUserA }
        }

        val allStudentsResponse = LectureResponse.signedUpOf(students)
        val clubAStudentsResponse = LectureResponse.signedUpOf(clubAStudents)
        val clubBStudentsResponse = LectureResponse.signedUpOf(clubBStudents)

        every { teacherRepository.findByUser(teacherUser) } returns teacher
        every { bbozzakRepository.findByUser(bbozzakUser) } returns bbozzak

        every { registeredLectureRepository.findSignedUpStudentsByLectureIdAndClubId(lectureId, clubAId) } returns clubAStudents
        every { registeredLectureRepository.findSignedUpStudentsByLectureIdAndClubId(lectureId, clubBId) } returns clubBStudents
        every { registeredLectureRepository.findSignedUpStudentsByLectureId(lectureId) } returns students

        every { lectureRepository.findByIdOrNull(lectureId) } returns lecture

        When("현재 로그인 한 유저가 Teacher라면"){
            every { userUtil.queryCurrentUser() } returns teacherUser

            val result = lectureServiceImpl.queryAllSignedUpStudents(lectureId)
            Then("result와 response가 같아야 한다") {
                result shouldBe clubAStudentsResponse
            }
        }

        When("현재 로그인 한 유저가 Bbozzak라면"){
            every { userUtil.queryCurrentUser() } returns bbozzakUser

            val result = lectureServiceImpl.queryAllSignedUpStudents(lectureId)
            Then("result와 response가 같아야 한다") {
                result shouldBe clubBStudentsResponse
            }
        }

        When("현재 로그인 한 유저가 Admin이라면") {
            every { userUtil.queryCurrentUser() } returns adminUser

            val result = lectureServiceImpl.queryAllSignedUpStudents(lectureId)
            Then("result와 response가 같아야 한다") {
                result shouldBe allStudentsResponse
            }
        }

        When("현재 로그인 한 유저가 PROFESSOR, COMPANY_INSTRUCTOR, GOVERNMENT이고 강의 강사라면") {
            every { userUtil.queryCurrentUser() } returns professorUserA

            val result = lectureServiceImpl.queryAllSignedUpStudents(lectureId)
            Then("result와 response가 같아야 한다") {
                result shouldBe allStudentsResponse
            }
        }

        When("현재 로그인 한 유저가 PROFESSOR, COMPANY_INSTRUCTOR, GOVERNMENT이고 강의 강사가 아니라면"){
            every { userUtil.queryCurrentUser() } returns professorUserB

            Then("ForbiddenCompletedLectureException이 발생해야 한다.") {
                shouldThrow<ForbiddenSignedUpLectureException> {
                    lectureServiceImpl.queryAllSignedUpStudents(lectureId)
                }
            }
        }

    }

})