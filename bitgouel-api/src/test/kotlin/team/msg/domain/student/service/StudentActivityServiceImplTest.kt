package team.msg.domain.student.service

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
import team.msg.domain.club.model.Club
import team.msg.domain.student.exception.ForbiddenStudentActivityException
import team.msg.domain.student.exception.StudentNotFoundException
import team.msg.domain.student.model.Student
import team.msg.domain.student.model.StudentActivity
import team.msg.domain.student.presentation.data.request.CreateStudentActivityRequest
import team.msg.domain.student.presentation.data.request.UpdateStudentActivityRequest
import team.msg.domain.student.presentation.data.response.StudentActivitiesResponse
import team.msg.domain.student.presentation.data.response.StudentActivityResponse
import team.msg.domain.student.repository.StudentActivityRepository
import team.msg.domain.student.repository.StudentRepository
import team.msg.domain.teacher.model.Teacher
import team.msg.domain.teacher.repository.TeacherRepository
import team.msg.domain.user.model.User
import java.time.LocalDate
import java.util.*

class StudentActivityServiceImplTest : BehaviorSpec({
    isolationMode = IsolationMode.InstancePerLeaf
    val fixture = kotlinFixture()

    val userUtil = mockk<UserUtil>()
    val studentRepository = mockk<StudentRepository>()
    val teacherRepository = mockk<TeacherRepository>()
    val studentActivityRepository = mockk<StudentActivityRepository>()
    val pageable = mockk<Pageable>()
    val studentActivityServiceImpl = StudentActivityServiceImpl(
        userUtil,
        studentRepository,
        teacherRepository,
        studentActivityRepository
    )

    // createStudentActivity 테스트 코드
    Given("CreateStudentStudentActivityRequest 가 주어질 때") {
        val user = fixture<User>()
        val club = fixture<Club>()
        val student = fixture<Student> {
            property(Student::club) { club }
        }
        val teacher = fixture<Teacher>()
        val studentActivity = fixture<StudentActivity>()
        val request = fixture<CreateStudentActivityRequest>()

        every { userUtil.queryCurrentUser() } returns user
        every { studentRepository.findByUser(user) } returns student
        every { teacherRepository.findByClub(student.club) } returns teacher
        every { studentActivityRepository.save(any()) } returns studentActivity

        When("학생 활동 추가 요청 시") {
            studentActivityServiceImpl.createStudentActivity(request)

            Then("Student Activity 가 저장이 되어야 한다") {
                verify(exactly = 1) { studentActivityRepository.save(any()) }
            }
        }

        When("Club에 속한 Teacher가 없다면") {
            every { teacherRepository.findByClub(club) } returns null

            Then("TeacherNotFoundException 이 발생해야 한다") {
                studentActivityServiceImpl.createStudentActivity(request)
            }
        }

        When("User 가 Student 가 아니라면") {
            every { studentRepository.findByUser(user) } returns null

            Then("StudentNotFoundException 이 발생해야 한다") {
                shouldThrow<StudentNotFoundException> {
                    studentActivityServiceImpl.createStudentActivity(request)
                }
            }
        }
    }

    // updateStudentActivity 테스트 코드
    Given("StudentActivityId와 UpdateStudentActivityRequest 가 주어질 때") {
        val studentActivityId = UUID.randomUUID()
        val request = fixture<UpdateStudentActivityRequest>()

        val user = fixture<User>()
        val student = fixture<Student>()
        val invalidStudent = fixture<Student>()
        val studentActivity = fixture<StudentActivity> {
            property(StudentActivity::student) { student }
        }

        every { userUtil.queryCurrentUser() } returns user
        every { studentRepository.findByUser(user) } returns student
        every { studentActivityRepository.save(any()) } returns studentActivity

        When("학생 활동 수정 요청 시") {
            studentActivityServiceImpl.updateStudentActivity(studentActivityId,request)

            Then("수정된 StudentActivity 가 저장이 되어야 한다") {
                verify(exactly = 1) { studentActivityRepository.save(any()) }
            }
        }

        When("User 가 Student 가 아니라면") {
            every { studentRepository.findByUser(user) } returns null

            Then("StudentNotFoundException 이 발생해야 한다") {
                shouldThrow<StudentNotFoundException> {
                    studentActivityServiceImpl.updateStudentActivity(studentActivityId,request)
                }
            }
        }

        When("User 자신이 작성한 StudentActivity 가 아니라면") {
            every { studentRepository.findByUser(user) } returns invalidStudent

            Then("ForbiddenStudentActivityException 이 발생해야 한다") {
                shouldThrow<ForbiddenStudentActivityException> {
                    studentActivityServiceImpl.updateStudentActivity(studentActivityId, request)
                }
            }
        }
    }

    // deleteStudentActivity 테스트 코드
    Given("StudentActivityId 가 주어질 때") {
        val studentActivityId = UUID.randomUUID()

        val user = fixture<User>()
        val student = fixture<Student>()
        val invalidStudent = fixture<Student>()
        val studentActivity = fixture<StudentActivity> {
            property(StudentActivity::student) { student }
        }

        every { userUtil.queryCurrentUser() } returns user
        every { studentRepository.findByUser(user) } returns student
        every { studentActivityRepository.findByIdOrNull(studentActivityId) } returns studentActivity

        When("학생 활동 삭제 요청 시") {
            studentActivityServiceImpl.deleteStudentActivity(studentActivityId)

            Then("Student Activity 가 삭제가 되어야 한다") {
                verify(exactly = 1) { studentActivityRepository.delete(any()) }
            }
        }

        When("User 가 Student 가 아니라면") {
            every { studentRepository.findByUser(user) } returns null

            Then("StudentNotFoundException 이 발생해야 한다") {
                shouldThrow<StudentNotFoundException> {
                    studentActivityServiceImpl.deleteStudentActivity(studentActivityId)
                }
            }
        }

        When("User 자신이 작성한 StudentActivity 가 아니라면") {
            every { studentRepository.findByUser(user) } returns invalidStudent

            Then("ForbiddenStudentActivityException 이 발생해야 한다") {
                shouldThrow<ForbiddenStudentActivityException> {
                    studentActivityServiceImpl.deleteStudentActivity(studentActivityId)
                }
            }
        }
    }

    // queryAllStudentActivity 테스트 코드
    Given("Pageable 이 주어질 때") {
        val studentActivityId = UUID.randomUUID()
        val pageable = fixture<Pageable>()

        val user = fixture<User>()
        val studentActivity = fixture<StudentActivity> {
            property(StudentActivity::id) { studentActivityId }
        }
        val studentActivityResponse = fixture<StudentActivityResponse> {
            property(StudentActivityResponse::activityId) { studentActivityId }
        }
        val response = fixture<StudentActivitiesResponse> {
            property(StudentActivitiesResponse::activities) { PageImpl(listOf(studentActivityResponse)) }
        }

        every { userUtil.queryCurrentUser() } returns user
        every { studentActivityRepository.findAll(any<Pageable>()) } returns PageImpl(listOf(studentActivity))

        When("학생 활동 전체 요청 시") {
            val result = studentActivityServiceImpl.queryAllStudentActivities(pageable)

            Then("result와 response가 같아야 한다") {
                result shouldBe response
            }
        }
    }

    // queryStudentActivitiesByStudent 테스트 코드
    Given("StudentId와 Pageable 이 주어질 때") {
        val studentActivityId = UUID.randomUUID()
        val title = "title"
        val activityDate = LocalDate.MAX
        val studentId = UUID.randomUUID()
        val userId = UUID.randomUUID()
        val username = "박주홍"

        val user = fixture<User> {
            property(User::id) { userId }
            property(User::name) { username }
        }
        val club = fixture<Club>()
        val student = fixture<Student> {
            property(Student::id) { studentId }
            property(Student::club) { club }
            property(Student::user) { user }
        }
        val teacher = fixture<Teacher> {
            property(Teacher::club) { club }
        }
        val invalidTeacher = fixture<Teacher>()
        val studentActivity = fixture<StudentActivity> {
            property(StudentActivity::id) { studentActivityId }
            property(StudentActivity::title) { title }
            property(StudentActivity::activityDate) { activityDate }
        }
        val studentActivityResponse = fixture<StudentActivityResponse> {
            property(StudentActivityResponse::activityId) { studentActivityId }
            property(StudentActivityResponse::title) { title }
            property(StudentActivityResponse::activityDate) { activityDate }
            property(StudentActivityResponse::userId) { userId }
            property(StudentActivityResponse::username) { username }
        }
        val response = fixture<StudentActivitiesResponse> {
            property(StudentActivitiesResponse::activities) { PageImpl(listOf(studentActivityResponse))}
        }

        every { userUtil.queryCurrentUser() } returns user
        every { studentRepository.findByIdOrNull(studentId) } returns student
        every { teacherRepository.findByUser(user) } returns teacher
        every { studentActivityRepository.findAllByStudent(student, pageable) } returns PageImpl(listOf(studentActivity))

        When("학생별 학생 활동 요청 시") {
            val result = studentActivityServiceImpl.queryStudentActivitiesByStudent(studentId, pageable)

            Then("result와 response가 같아야 한다") {
                result shouldBe response
            }
        }

        When("유효하지 않은 StudentId 라면") {
            every { studentRepository.findByIdOrNull(studentId) } returns null

            Then("StudentNotFoundException 가 발생해야 한다") {
                shouldThrow<StudentNotFoundException> {
                    studentActivityServiceImpl.queryStudentActivitiesByStudent(studentId, pageable)
                }
            }
        }

        When("조회 하려는 학생과 조회 하려는 선생님의 동아리가 다르다면") {
            every { teacherRepository.findByUser(user) } returns invalidTeacher

            Then("ForbiddenStudentActivity 가 발생해야 한다") {
                shouldThrow<ForbiddenStudentActivityException> {
                    studentActivityServiceImpl.queryStudentActivitiesByStudent(studentId, pageable)
                }
            }
        }
    }
})