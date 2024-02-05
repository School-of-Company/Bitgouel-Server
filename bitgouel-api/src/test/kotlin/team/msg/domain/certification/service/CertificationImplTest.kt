package team.msg.domain.certification.service

import com.appmattus.kotlinfixture.kotlinFixture
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import team.msg.common.util.UserUtil
import team.msg.domain.admin.exception.AdminNotFoundException
import team.msg.domain.admin.model.Admin
import team.msg.domain.admin.repository.AdminRepository
import team.msg.domain.certifiacation.model.Certification
import team.msg.domain.certifiacation.repository.CertificationRepository
import team.msg.domain.certification.presentation.data.request.CreateCertificationRequest
import team.msg.domain.faq.model.Faq
import team.msg.domain.faq.presentation.data.request.CreateFaqRequest
import team.msg.domain.faq.repository.FaqRepository
import team.msg.domain.faq.service.FaqServiceImpl
import team.msg.domain.student.exception.StudentNotFoundException
import team.msg.domain.student.model.Student
import team.msg.domain.student.repository.StudentRepository
import team.msg.domain.teacher.repository.TeacherRepository
import team.msg.domain.user.model.User

class CertificationImplTest : BehaviorSpec ({

    isolationMode = IsolationMode.InstancePerLeaf
    val fixture = kotlinFixture()

    val certificationRepository = mockk<CertificationRepository>()
    val studentRepository = mockk<StudentRepository>()
    val userUtil = mockk<UserUtil>()
    val teacherRepository = mockk<TeacherRepository>()
    val certificationServiceImpl = CertificationServiceImpl(
        certificationRepository,
        studentRepository,
        userUtil,
        teacherRepository
    )

    // createCertification 테스트 코드
    Given("CreateCertificationRequest 가 주어질 때") {
        val user = fixture<User>()
        val student = fixture<Student>()
        val certification = fixture<Certification>()
        val request = fixture<CreateCertificationRequest>()

        every { userUtil.queryCurrentUser() } returns user
        every { studentRepository.findByUser(user) } returns student
        every { certificationRepository.save(any()) } returns certification

        When("자격증 등록 요청을 하면") {
            certificationServiceImpl.createCertification(request)

            Then("Certification 이 저장이 되어야 한다.") {
                verify(exactly = 1) { certificationRepository.save(any()) }
            }
        }

        When("현재 유저가 학생이 아니라면") {
            every { studentRepository.findByUser(user) } returns null

            Then("StudentNotFoundException 가 터져야 한다.") {
                shouldThrow<StudentNotFoundException> {
                    certificationServiceImpl.createCertification(request)
                }
            }
        }
    }
})