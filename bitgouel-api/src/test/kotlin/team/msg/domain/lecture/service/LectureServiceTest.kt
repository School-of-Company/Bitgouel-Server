package team.msg.domain.lecture.service

import com.appmattus.kotlinfixture.kotlinFixture
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.data.repository.findByIdOrNull
import team.msg.common.util.UserUtil
import team.msg.domain.lecture.model.Lecture
import team.msg.domain.lecture.presentation.data.request.CreateLectureRequest
import team.msg.domain.lecture.repository.LectureRepository
import team.msg.domain.lecture.repository.RegisteredLectureRepository
import team.msg.domain.student.repository.StudentRepository
import team.msg.domain.user.exception.UserNotFoundException
import team.msg.domain.user.model.User
import team.msg.domain.user.repository.UserRepository

class LectureServiceTest : BehaviorSpec({

    isolationMode = IsolationMode.InstancePerLeaf
    val fixture = kotlinFixture()

    val lectureRepository = mockk<LectureRepository>()
    val registeredLectureRepository = mockk<RegisteredLectureRepository>()
    val studentRepository = mockk<StudentRepository>()
    val userRepository = mockk<UserRepository>()
    val userUtil = mockk<UserUtil>()
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

        every { lectureRepository.save(any()) } returns lecture
        every { userRepository.findByIdOrNull(any()) } returns user

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
})