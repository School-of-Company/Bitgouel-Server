package team.msg.domain.certification.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.msg.common.util.UserUtil
import team.msg.domain.certifiacation.model.Certification
import team.msg.domain.certifiacation.repository.CertificationRepository
import team.msg.domain.certification.exception.ForbiddenCertificationException
import team.msg.domain.certification.presentation.data.request.CreateCertificationRequest
import team.msg.domain.student.exception.StudentNotFoundException
import team.msg.domain.student.repository.StudentRepository
import team.msg.domain.teacher.exception.TeacherNotFoundException
import team.msg.domain.teacher.repository.TeacherRepository
import team.msg.domain.user.enums.Authority
import java.util.*

@Service
class CertificationServiceImpl(
    private val certificationRepository: CertificationRepository,
    private val studentRepository: StudentRepository,
    private val userUtil: UserUtil,
    private val teacherRepository: TeacherRepository
) : CertificationService {
    /**
     * 자격증에 대한 정보를 작성하는 비지니스 로직입니다.
     * @param 자격증을 소지한 학생 id
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun createCertification(studentId: UUID, createCertificationRequest: CreateCertificationRequest) {
        val user = userUtil.queryCurrentUser()

        when (user.authority) {
            Authority.ROLE_TEACHER -> {
                val teacher = teacherRepository.findByUser(user)
                    ?: throw TeacherNotFoundException("존재하지 않는 선생님입니다. info : [ userId = ${user.id}")
                val student = studentRepository.findStudentById(studentId)
                    ?: throw StudentNotFoundException("존재하지 않는 학생입니다. info : [ studentId = $studentId ]")

                if (teacher.club != student.club)
                    throw ForbiddenCertificationException("유효하지 않은 권한입니다. info : [ userAuthority = ${user.authority} ]")
            }
            Authority.ROLE_STUDENT -> {
                if (!studentRepository.existOne(studentId))
                    throw StudentNotFoundException("존재하지 않는 학생입니다. info : [ studentId = $studentId ]")
            }
            else -> throw ForbiddenCertificationException("유효하지 않은 권한입니다. info : [ userAuthority = ${user.authority} ]")
        }

        val certification = Certification(
            id = UUID.randomUUID(),
            studentId = studentId,
            name = createCertificationRequest.name,
            acquisitionDate = createCertificationRequest.acquisitionDate
        )

        certificationRepository.save(certification)
    }
}