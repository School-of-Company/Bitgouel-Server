package team.msg.domain.certification.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.msg.common.util.UserUtil
import team.msg.domain.certifiacation.model.Certification
import team.msg.domain.certifiacation.repository.CertificationRepository
import team.msg.domain.certification.exception.CertificationNotFoundException
import team.msg.domain.certification.exception.ForbiddenCertificationException
import team.msg.domain.certification.presentation.data.request.CreateCertificationRequest
import team.msg.domain.certification.presentation.data.request.UpdateCertificationRequest
import team.msg.domain.certification.presentation.data.response.CertificationResponse
import team.msg.domain.certification.presentation.data.response.CertificationsResponse
import team.msg.domain.student.exception.StudentNotFoundException
import team.msg.domain.student.model.Student
import team.msg.domain.student.repository.StudentRepository
import team.msg.domain.teacher.exception.TeacherNotFoundException
import team.msg.domain.teacher.model.Teacher
import team.msg.domain.teacher.repository.TeacherRepository
import team.msg.domain.user.model.User
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
     * 학생이 자격증 리스트를 조회하는 비지니스 로직입니다.
     * @return 학생의 자격증 리스트
     */
    @Transactional(readOnly = true)
    override fun queryCertifications(): CertificationsResponse {
        val user = userUtil.queryCurrentUser()

        val student = studentRepository findByUer user

        val certifications = certificationRepository findAllByStudentId student.id

        val response = CertificationsResponse(
            CertificationResponse.listOf(certifications)
        )

        return response
    }

    /**
     * 선생님이 자격증 리스트를 조회하는 비지니스 로직입니다.
     * @param 자격증 리스트를 조회하기 위한 학생 id
     * @return 학생의 자격증 리스트
     */
    @Transactional(readOnly = true)
    override fun queryCertifications(studentId: UUID): CertificationsResponse {
        val user = userUtil.queryCurrentUser()

        val teacher = teacherRepository findByUser user

        val student = studentRepository findStudentById studentId

        if (student.club != teacher.club)
            throw ForbiddenCertificationException("자격증을 조회할 권한이 없습니다. info : [ club = ${teacher.club} ]")

        val certifications = certificationRepository findAllByStudentId studentId

        val response = CertificationsResponse(
            CertificationResponse.listOf(certifications)
        )

        return response
    }

    /**
     * 자격증을 수정하는 비지니스 로직입니다.
     * @param 자격증 id, 수정할 자격증의 내용
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun updateCertification(id: UUID, updateCertificationRequest: UpdateCertificationRequest) {
        val user = userUtil.queryCurrentUser()
        val student = studentRepository findByUer user

        val certification = certificationRepository findById id

        if (student.id != certification.studentId)
            throw ForbiddenCertificationException("자격증을 수정할 권한이 없습니다. info : [ studentId = ${student.id} ]")

        val updateCertification = Certification(
            id = certification.id,
            studentId = certification.studentId,
            name = updateCertificationRequest.name,
            acquisitionDate = updateCertificationRequest.acquisitionDate
        )

        certificationRepository.save(updateCertification)
    }

    private infix fun StudentRepository.findByUer(user: User): Student =
        this.findByUser(user)
            ?: throw StudentNotFoundException("존재하지 않는 학생입니다. info : [ userId = ${user.id} ]")

    private infix fun StudentRepository.findStudentById(studentId: UUID): Student =
        this.findStudentById(studentId)
            ?: throw StudentNotFoundException("존재하지 않는 학생입니다. info : [ studentId = $studentId ]")

    private infix fun CertificationRepository.findAllByStudentId(studentId: UUID): List<Certification> =
        this.findAllByStudentId(studentId)

    private infix fun CertificationRepository.findById(id: UUID): Certification =
        this.findByIdOrNull(id)
            ?: throw CertificationNotFoundException("존재하지 않는 자격증입니다. info : [ certificationId = $id ]")

    private infix fun TeacherRepository.findByUser(user: User): Teacher =
        this.findByUser(user)
            ?: throw TeacherNotFoundException("존재하지 않는 선생님입니다. info : [ userId = ${user.id} ]")
}