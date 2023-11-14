package team.msg.domain.certification.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.msg.domain.certifiacation.model.Certification
import team.msg.domain.certifiacation.repository.CertificationRepository
import team.msg.domain.certification.presentation.data.request.CreateCertificationRequest
import team.msg.domain.student.exception.StudentNotFoundException
import team.msg.domain.student.repository.StudentRepository
import java.util.*

@Service
class CertificationServiceImpl(
    private val certificationRepository: CertificationRepository,
    private val studentRepository: StudentRepository
) : CertificationService {

    /**
     * 자격증에 대한 정보를 작성하는 비지니스 로직입니다.
     * @param 자격증을 소지한 학생 id
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun createCertification(studentId: UUID, request: CreateCertificationRequest) {
        studentRepository existOne studentId

        val certification = Certification(
            id = UUID.randomUUID(),
            studentId = studentId,
            name = request.name,
            acquisitionDate = request.acquisitionDate
        )

        certificationRepository.save(certification)
    }

    private infix fun StudentRepository.existOne(studentId: UUID) {
        if (!this.existOne(studentId))
            throw StudentNotFoundException("존재하지 않는 학생입니다. info : [ studentId = $studentId ]")
    }
}