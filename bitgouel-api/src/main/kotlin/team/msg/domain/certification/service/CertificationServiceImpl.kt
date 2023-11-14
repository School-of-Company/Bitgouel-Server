package team.msg.domain.certification.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.msg.common.util.UserUtil
import team.msg.domain.certifiacation.model.Certification
import team.msg.domain.certifiacation.repository.CertificationRepository
import team.msg.domain.certification.presentation.data.request.CreateCertificationRequest
import team.msg.domain.certification.presentation.data.response.AllCertificationsResponse
import team.msg.domain.certification.presentation.data.response.CertificationResponse
import team.msg.domain.student.exception.StudentNotFoundException
import team.msg.domain.student.model.Student
import team.msg.domain.student.repository.StudentRepository
import team.msg.domain.user.model.User
import java.util.*

@Service
class CertificationServiceImpl(
    private val certificationRepository: CertificationRepository,
    private val studentRepository: StudentRepository,
    private val userUtil: UserUtil
) : CertificationService {

    /**
     * 자격증에 대한 정보를 작성하는 비지니스 로직입니다.
     * @param 자격증을 소지한 학생 id
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun createCertification(request: CreateCertificationRequest) {
        val user = userUtil.queryCurrentUser()
        val student = studentRepository findByUer user

        val certification = Certification(
            id = UUID.randomUUID(),
            studentId = student.id,
            name = request.name,
            acquisitionDate = request.acquisitionDate
        )

        certificationRepository.save(certification)
    }

    /**
     * 자격증 리스트를 조회하는 비지니스 로직입니다.
     */
    @Transactional(readOnly = true)
    override fun queryAllCertifications(): AllCertificationsResponse {
        val user = userUtil.queryCurrentUser()

        val student = studentRepository findByUer user

        val certifications = certificationRepository.findAllByStudentId(student.id)

        val response = AllCertificationsResponse(
            CertificationResponse.listOf(certifications)
        )

        return response
    }

    private infix fun StudentRepository.findByUer(user: User): Student =
        this.findByUser(user)
            ?: throw StudentNotFoundException("존재하지 않는 유저입니다. info : [ userId = ${user.id} ]")
}