package team.msg.domain.student.service

import com.appmattus.kotlinfixture.kotlinFixture
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.data.repository.findByIdOrNull
import team.msg.common.util.UserUtil
import team.msg.domain.club.model.Club
import team.msg.domain.student.exception.ForbiddenStudentActivityException
import team.msg.domain.student.exception.StudentNotFoundException
import team.msg.domain.student.model.Student
import team.msg.domain.student.model.StudentActivity
import team.msg.domain.student.presentation.data.request.CreateStudentActivityRequest
import team.msg.domain.student.presentation.data.request.UpdateStudentActivityRequest
import team.msg.domain.student.repository.StudentActivityRepository
import team.msg.domain.student.repository.StudentRepository
import team.msg.domain.teacher.model.Teacher
import team.msg.domain.teacher.repository.TeacherRepository
import team.msg.domain.user.model.User
import java.util.*

class StudentActivityServiceImplTest : BehaviorSpec({
    isolationMode = IsolationMode.InstancePerLeaf
    val fixture = kotlinFixture()

    val userUtil = mockk<UserUtil>()
    val studentRepository = mockk<StudentRepository>()
    val teacherRepository = mockk<TeacherRepository>()
    val studentActivityRepository = mockk<StudentActivityRepository>()
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
})