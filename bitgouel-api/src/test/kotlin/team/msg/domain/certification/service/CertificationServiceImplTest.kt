package team.msg.domain.certification.service

import com.appmattus.kotlinfixture.kotlinFixture
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.data.repository.findByIdOrNull
import team.msg.common.util.UserUtil
import team.msg.domain.certifiacation.model.Certification
import team.msg.domain.certifiacation.repository.CertificationRepository
import team.msg.domain.certification.exception.ForbiddenCertificationException
import team.msg.domain.certification.presentation.data.request.CreateCertificationRequest
import team.msg.domain.certification.presentation.data.request.UpdateCertificationRequest
import team.msg.domain.certification.presentation.data.response.CertificationResponse
import team.msg.domain.certification.presentation.data.response.CertificationsResponse
import team.msg.domain.club.model.Club
import team.msg.domain.student.exception.StudentNotFoundException
import team.msg.domain.student.model.Student
import team.msg.domain.student.repository.StudentRepository
import team.msg.domain.teacher.exception.TeacherNotFoundException
import team.msg.domain.teacher.model.Teacher
import team.msg.domain.teacher.repository.TeacherRepository
import team.msg.domain.user.model.User
import java.time.LocalDate
import java.util.*

class CertificationServiceImplTest : BehaviorSpec ({

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

            Then("StudentNotFoundException이 발생해야 한다.") {
                shouldThrow<StudentNotFoundException> {
                    certificationServiceImpl.createCertification(request)
                }
            }
        }
    }

    // queryCertifications 테스트 코드
    Given("Certification 이 주어질 때") {
        val certificationId = UUID.randomUUID()
        val certificationName = "자격증"
        val certificationAcquisitionDate = LocalDate.MAX

        val user = fixture<User>()
        val student = fixture<Student>()
        val certification = fixture<Certification> {
            property(Certification::id) { certificationId }
            property(Certification::name) { certificationName }
            property(Certification::acquisitionDate) { certificationAcquisitionDate }
        }
        val certificationResponse = fixture<CertificationResponse> {
            property(CertificationResponse::id) { certificationId }
            property(CertificationResponse::name) { certificationName }
            property(CertificationResponse::acquisitionDate) { certificationAcquisitionDate }
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

            Then("StudentNotFoundException이 발생해야 한다.") {
                shouldThrow<StudentNotFoundException> {
                    certificationServiceImpl.queryCertifications()
                }
            }
        }
    }

    // queryCertifications 테스트 코드
    Given("Certification 이 주어질 때") {
        val certificationId = UUID.randomUUID()
        val certificationName = "자격증"
        val certificationAcquisitionDate = LocalDate.MAX
        val request = UUID.randomUUID()

        val club = fixture<Club>()
        val user = fixture<User>()
        val teacher = fixture<Teacher> {
            property(Teacher::club) { club }
        }
        val invalidTeacher = fixture<Teacher>()
        val student = fixture<Student> {
            property(Student::club) { club }
        }
        val certification = fixture<Certification> {
            property(Certification::id) { certificationId }
            property(Certification::name) { certificationName }
            property(Certification::acquisitionDate) { certificationAcquisitionDate }
        }
        val certificationResponse = fixture<CertificationResponse> {
            property(CertificationResponse::id) { certificationId }
            property(CertificationResponse::name) { certificationName }
            property(CertificationResponse::acquisitionDate) { certificationAcquisitionDate }
        }
        val response = fixture<CertificationsResponse> {
            property(CertificationsResponse::certifications) { listOf(certificationResponse) }
        }

        every { userUtil.queryCurrentUser() } returns user
        every { teacherRepository.findByUser(user) } returns teacher
        every { studentRepository.findStudentById(request) } returns student
        every { certificationRepository.findAllByStudentIdOrderByAcquisitionDateDesc(request) } returns listOf(certification)

        When("자격증 전체 조회 요청을 하면") {
            val result = certificationServiceImpl.queryCertifications(request)

            Then("result와 response가 같아야 한다.") {
                result shouldBe response
            }
        }

        When("현재 유저가 선생님이 아니라면") {
            every { teacherRepository.findByUser(user) } returns null

            Then("TeacherNotFoundException이 발생해야 한다.") {
                shouldThrow<TeacherNotFoundException> {
                    certificationServiceImpl.queryCertifications(request)
                }
            }
        }

        When("request 에 맞는 학생이 없다면") {
            every { studentRepository.findStudentById(request) } returns null

            Then("StudentNotFoundException이 발생해야 한다.") {
                shouldThrow<StudentNotFoundException> {
                    certificationServiceImpl.queryCertifications(request)
                }
            }
        }

        When("선생님 소속 동아리가 아닌 학생의 자격증을 조회한다면") {
            every { teacherRepository.findByUser(user) } returns invalidTeacher

            Then("ForbiddenCertificationException이 발생해야 한다.") {
                shouldThrow<ForbiddenCertificationException> {
                    certificationServiceImpl.queryCertifications(request)
                }
            }
        }
    }

    // updateCertification 테스트 코드
    Given("certificationId와 updateCertificationRequest 가 주어질 때") {
        val certificationId = UUID.randomUUID()
        val request = fixture<UpdateCertificationRequest>()

        val club = fixture<Club>()
        val user = fixture<User>()
        val invalidStudent = fixture<Student>()
        val student = fixture<Student> {
            property(Student::club) { club }
        }
        val certification = fixture<Certification> {
            property(Certification::studentId) { student.id }
        }

        every { userUtil.queryCurrentUser() } returns user
        every { studentRepository.findByUser(user) } returns student
        every { certificationRepository.findByIdOrNull(certificationId) } returns certification
        every { certificationRepository.save(certification) } returns certification

        When("자격증 수정 요청을 하면") {
            certificationServiceImpl.updateCertification(certificationId, request)

            Then("result와 response가 같아야 한다.") {
                verify(exactly = 1) { certificationRepository.save(certification) }
            }
        }

        When("현재 유저가 학생이 아니라면") {
            every { studentRepository.findByUser(user) } returns null

            Then("StudentNotFoundException 가 터져야 한다.") {
                shouldThrow<StudentNotFoundException> {
                    certificationServiceImpl.updateCertification(certificationId, request)
                }
            }
        }

        When("자신의 자격증이 아니라면") {
            every { studentRepository.findByUser(user) } returns invalidStudent

            Then("ForbiddenCertificationException 가 터져야 한다.") {
                shouldThrow<ForbiddenCertificationException> {
                    certificationServiceImpl.updateCertification(certificationId, request)
                }
            }
        }
    }
})