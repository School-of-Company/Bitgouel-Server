package team.msg.domain.certification.service

import com.appmattus.kotlinfixture.kotlinFixture
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import team.msg.common.util.UserUtil
import team.msg.domain.certifiacation.model.Certification
import team.msg.domain.certifiacation.repository.CertificationRepository
import team.msg.domain.certification.presentation.data.request.CreateCertificationRequest
import team.msg.domain.certification.presentation.data.response.CertificationResponse
import team.msg.domain.certification.presentation.data.response.CertificationsResponse
import team.msg.domain.student.exception.StudentNotFoundException
import team.msg.domain.student.model.Student
import team.msg.domain.student.repository.StudentRepository
import team.msg.domain.teacher.repository.TeacherRepository
import team.msg.domain.user.model.User
import java.time.LocalDate
import java.util.*

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

    // queryCertifications 테스트 코드
    Given("Certification 이 주어질 때") {
        val user = fixture<User>()
        val student = fixture<Student>()
        val certification = fixture<Certification> {
            property(Certification::id) { UUID.randomUUID() }
            property(Certification::name) { "자격증" }
            property(Certification::acquisitionDate) { LocalDate.MAX }
        }
        val certificationResponse = fixture<CertificationResponse> {
            property(CertificationResponse::id) { certification.id }
            property(CertificationResponse::name) { "자격증" }
            property(CertificationResponse::acquisitionDate) { LocalDate.MAX }
        }
        val response = fixture<CertificationsResponse> {
            property(CertificationsResponse::certifications) { listOf(certificationResponse) }
        }

        every { userUtil.queryCurrentUser() } returns user
        every { studentRepository.findByUser(user) } returns student
        every { certificationRepository.findAllByStudentIdOrderByAcquisitionDateDesc(student.id) } returns listOf(certification)

        When("자격증 전체 조회 요청을 하면") {
            val result = certificationServiceImpl.queryCertifications()

            Then("result와 response가 같아야 한다.") {
                result shouldBe response
            }
        }

        When("현재 유저가 학생이 아니라면") {
            every { studentRepository.findByUser(user) } returns null

            Then("StudentNotFoundException 가 터져야 한다.") {
                shouldThrow<StudentNotFoundException> {
                    certificationServiceImpl.queryCertifications()
                }
            }
        }
    }
})