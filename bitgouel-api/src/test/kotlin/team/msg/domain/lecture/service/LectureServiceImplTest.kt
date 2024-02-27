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
import team.msg.domain.lecture.enums.LectureStatus
import team.msg.domain.lecture.enums.LectureType
import team.msg.domain.lecture.exception.AlreadySignedUpLectureException
import team.msg.domain.lecture.exception.OverMaxRegisteredUserException
import team.msg.domain.lecture.exception.UnSignedUpLectureException
import team.msg.domain.lecture.model.Lecture
import team.msg.domain.lecture.model.RegisteredLecture
import team.msg.domain.lecture.presentation.data.request.CreateLectureRequest
import team.msg.domain.lecture.presentation.data.request.QueryAllLectureRequest
import team.msg.domain.lecture.presentation.data.response.LectureDetailsResponse
import team.msg.domain.lecture.presentation.data.response.LectureResponse
import team.msg.domain.lecture.presentation.data.response.LecturesResponse
import team.msg.domain.lecture.repository.LectureRepository
import team.msg.domain.lecture.repository.RegisteredLectureRepository
import team.msg.domain.student.exception.StudentNotFoundException
import team.msg.domain.student.model.Student
import team.msg.domain.student.repository.StudentRepository
import team.msg.domain.user.enums.Authority
import team.msg.domain.user.exception.UserNotFoundException
import team.msg.domain.user.model.User
import team.msg.domain.user.repository.UserRepository
import java.time.LocalDateTime
import java.util.*

class LectureServiceImplTest : BehaviorSpec({

    isolationMode = IsolationMode.InstancePerLeaf
    val fixture = kotlinFixture()

    val lectureRepository = mockk<LectureRepository>()
    val registeredLectureRepository = mockk<RegisteredLectureRepository>()
    val studentRepository = mockk<StudentRepository>()
    val userRepository = mockk<UserRepository>()
    val userUtil = mockk<UserUtil>()
    val pageable = mockk<Pageable>()
    val lectureServiceImpl = LectureServiceImpl(
        lectureRepository,
        registeredLectureRepository,
        studentRepository,
        userRepository,
        userUtil
    )

    // createLecture 테스트 코드
    Given("CreateLectureRequest 가 주어질 때") {

        val user = fixture<User>()
        val request = fixture<CreateLectureRequest>()
        val lecture = fixture<Lecture>()

        every { userRepository.findByIdOrNull(any()) } returns user
        every { lectureRepository.save(any()) } returns lecture

        When("Lecture 등록 요청을 하면") {
            lectureServiceImpl.createLecture(request)

            Then("Lecture 가 저장이 되어야 한다.") {
                verify(exactly = 1) { lectureRepository.save(any()) }
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

        val creditLectureId = UUID.randomUUID()
        val creditLectureType = LectureType.MUTUAL_CREDIT_RECOGNITION_PROGRAM

        val universityLectureId = UUID.randomUUID()
        val universityLectureType = LectureType.UNIVERSITY_EXPLORATION_PROGRAM

        val creditLecture = fixture<Lecture> {
            property(Lecture::id) { creditLectureId }
            property(Lecture::name) { name }
            property(Lecture::content) { content }
            property(Lecture::lectureType) { creditLectureType }
            property(Lecture::maxRegisteredUser) { maxRegisteredUser }
            property(Lecture::startDate) { startDate }
            property(Lecture::endDate) { endDate }
            property(Lecture::completeDate) { completeDate }
            property(Lecture::instructor) { instructor }
        }
        val universityLecture = fixture<Lecture> {
            property(Lecture::id) { universityLectureId }
            property(Lecture::name) { name }
            property(Lecture::content) { content }
            property(Lecture::lectureType) { universityLectureType }
            property(Lecture::maxRegisteredUser) { maxRegisteredUser }
            property(Lecture::startDate) { startDate }
            property(Lecture::endDate) { endDate }
            property(Lecture::completeDate) { completeDate }
            property(Lecture::instructor) { instructor }
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
            property(LectureResponse::completeDate) { completeDate }
            property(LectureResponse::lecturer) { instructor }
            property(LectureResponse::lectureStatus) { lectureStatus }
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
            property(LectureResponse::completeDate) { completeDate }
            property(LectureResponse::lecturer) { instructor }
            property(LectureResponse::lectureStatus) { lectureStatus }
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
        val headCount = 0
        val maxRegisteredUser = 5
        val credit = 2
        val startDate = LocalDateTime.MIN
        val endDate = LocalDateTime.MAX
        val completeDate = LocalDateTime.MAX
        val lectureStatus = LectureStatus.OPENED
        val lectureType = LectureType.MUTUAL_CREDIT_RECOGNITION_PROGRAM
        val isRegistered = false

        val lecture = fixture<Lecture> {
            property(Lecture::id) { lectureId }
            property(Lecture::name) { name }
            property(Lecture::content) { content }
            property(Lecture::lectureType) { lectureType }
            property(Lecture::maxRegisteredUser) { maxRegisteredUser }
            property(Lecture::startDate) { startDate }
            property(Lecture::endDate) { endDate }
            property(Lecture::completeDate) { completeDate }
            property(Lecture::instructor) { instructor }
            property(Lecture::credit) { credit }
        }

        val response = fixture<LectureDetailsResponse> {
            property(LectureDetailsResponse::name) { name }
            property(LectureDetailsResponse::content) { content }
            property(LectureDetailsResponse::lectureType) { lectureType }
            property(LectureDetailsResponse::headCount) { headCount }
            property(LectureDetailsResponse::maxRegisteredUser) { maxRegisteredUser }
            property(LectureDetailsResponse::startDate) { startDate }
            property(LectureDetailsResponse::endDate) { endDate }
            property(LectureDetailsResponse::completeDate) { completeDate }
            property(LectureDetailsResponse::lecturer) { instructor }
            property(LectureDetailsResponse::lectureStatus) { lectureStatus }
            property(LectureDetailsResponse::isRegistered) { isRegistered }
            property(LectureDetailsResponse::createAt) { lecture.createdAt }
            property(LectureDetailsResponse::credit) { credit }
        }

        every { userUtil.queryCurrentUser() } returns user
        every { lectureRepository.findByIdOrNull(lectureId) } returns lecture
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
        val completeDate = LocalDateTime.MAX
        val lectureType = LectureType.MUTUAL_CREDIT_RECOGNITION_PROGRAM

        val lecture = fixture<Lecture> {
            property(Lecture::id) { lectureId }
            property(Lecture::name) { name }
            property(Lecture::content) { content }
            property(Lecture::lectureType) { lectureType }
            property(Lecture::maxRegisteredUser) { maxRegisteredUser }
            property(Lecture::startDate) { startDate }
            property(Lecture::endDate) { endDate }
            property(Lecture::completeDate) { completeDate }
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
        val completeDate = LocalDateTime.MAX
        val lectureType = LectureType.MUTUAL_CREDIT_RECOGNITION_PROGRAM

        val lecture = fixture<Lecture> {
            property(Lecture::id) { lectureId }
            property(Lecture::name) { name }
            property(Lecture::content) { content }
            property(Lecture::lectureType) { lectureType }
            property(Lecture::maxRegisteredUser) { maxRegisteredUser }
            property(Lecture::startDate) { startDate }
            property(Lecture::endDate) { endDate }
            property(Lecture::completeDate) { completeDate }
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

        When("수강 신청을 하지 않았는데 수강 신청을 취소하면") {
            every { registeredLectureRepository.findByStudentAndLecture(any(), any()) } returns null

            Then("UnSignedUpLectureException이 발생해야 한다.") {
                shouldThrow<UnSignedUpLectureException> {
                    lectureServiceImpl.cancelSignUpLecture(lectureId)
                }
            }
        }
    }
})

