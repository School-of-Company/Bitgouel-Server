package team.msg.domain.student.service

import com.appmattus.kotlinfixture.kotlinFixture
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import team.msg.common.util.UserUtil
import team.msg.domain.student.exception.StudentNotFoundException
import team.msg.domain.student.model.Student
import team.msg.domain.student.model.StudentActivity
import team.msg.domain.student.presentation.data.request.CreateStudentActivityRequest
import team.msg.domain.student.repository.StudentActivityRepository
import team.msg.domain.student.repository.StudentRepository
import team.msg.domain.teacher.model.Teacher
import team.msg.domain.teacher.repository.TeacherRepository
import team.msg.domain.user.model.User

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
        val student = fixture<Student>()
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
})