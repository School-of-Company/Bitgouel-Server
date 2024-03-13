package team.msg.domain.certification.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.msg.common.util.UserUtil
import team.msg.domain.admin.model.Admin
import team.msg.domain.bbozzak.exception.BbozzakNotFoundException
import team.msg.domain.bbozzak.model.Bbozzak
import team.msg.domain.bbozzak.repository.BbozzakRepository
import team.msg.domain.certifiacation.model.Certification
import team.msg.domain.certifiacation.repository.CertificationRepository
import team.msg.domain.certification.exception.CertificationNotFoundException
import team.msg.domain.certification.exception.ForbiddenCertificationException
import team.msg.domain.certification.presentation.data.request.CreateCertificationRequest
import team.msg.domain.certification.presentation.data.request.UpdateCertificationRequest
import team.msg.domain.certification.presentation.data.response.CertificationResponse
import team.msg.domain.certification.presentation.data.response.CertificationsResponse
import team.msg.domain.company.exception.CompanyNotFoundException
import team.msg.domain.company.model.CompanyInstructor
import team.msg.domain.company.repository.CompanyInstructorRepository
import team.msg.domain.government.GovernmentNotFoundException
import team.msg.domain.government.model.Government
import team.msg.domain.government.repository.GovernmentRepository
import team.msg.domain.professor.exception.ProfessorNotFoundException
import team.msg.domain.professor.model.Professor
import team.msg.domain.professor.repository.ProfessorRepository
import team.msg.domain.student.exception.StudentNotFoundException
import team.msg.domain.student.model.Student
import team.msg.domain.student.repository.StudentRepository
import team.msg.domain.teacher.exception.TeacherNotFoundException
import team.msg.domain.teacher.model.Teacher
import team.msg.domain.teacher.repository.TeacherRepository
import team.msg.domain.user.model.User
import team.msg.global.exception.InvalidRoleException
import java.util.*

@Service
class CertificationServiceImpl(
    private val certificationRepository: CertificationRepository,
    private val studentRepository: StudentRepository,
    private val userUtil: UserUtil,
    private val teacherRepository: TeacherRepository,
    private val bbozzakRepository: BbozzakRepository,
    private val professorRepository: ProfessorRepository,
    private val companyInstructorRepository: CompanyInstructorRepository,
    private val governmentRepository: GovernmentRepository
) : CertificationService {

    /**
     * 자격증에 대한 정보를 작성하는 비지니스 로직입니다.
     * @param 자격증을 소지한 학생 id
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun createCertification(request: CreateCertificationRequest) {
        val user = userUtil.queryCurrentUser()
        val student = studentRepository findByUser user

        val certification = Certification(
            id = UUID.randomUUID(),
            studentId = student.id,
            name = request.name,
            acquisitionDate = request.acquisitionDate
        )

        certificationRepository.save(certification)
    }

    /**
     * 학생이 자신의 자격증 리스트를 조회하는 비지니스 로직입니다.
     * @return 학생의 자격증 리스트
     */
    @Transactional(readOnly = true)
    override fun queryCertifications(): CertificationsResponse {
        val user = userUtil.queryCurrentUser()

        val student = studentRepository findByUser user

        val certifications = certificationRepository findAllByStudentIdOrderByAcquisitionDateDesc student.id

        val response = CertificationsResponse(
            CertificationResponse.listOf(certifications)
        )

        return response
    }

    /**
     * 학생의 자격증 리스트를 조회하는 비지니스 로직입니다.
     * @param 자격증 리스트를 조회하기 위한 학생 id
     * @return 학생의 자격증 리스트
     */
    @Transactional(readOnly = true)
    override fun queryCertifications(studentId: UUID): CertificationsResponse {
        val user = userUtil.queryCurrentUser()

        val entity = userUtil.getAuthorityEntityAndOrganization(user).first

        val club = when(entity) {
            is Student -> findStudentByUser(user).club
            is Teacher -> findTeacherByUser(user).club
            is Bbozzak -> findBbozzakByUser(user).club
            is Professor -> findProfessorByUser(user).club
            is CompanyInstructor -> findCompanyInstructorByUser(user).club
            is Government -> findGovernmentByUser(user).club
            is Admin -> null
            else ->  throw InvalidRoleException("유효하지 않은 권한입니다. info : [ userAuthority = ${user.authority} ]")
        }

        val student = studentRepository findStudentById studentId

        if (student.club != club && club != null)
            throw ForbiddenCertificationException("자격증을 조회할 권한이 없습니다. info : [ club = $club ]")

        val certifications = certificationRepository findAllByStudentIdOrderByAcquisitionDateDesc studentId

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
        val student = studentRepository findByUser user

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

    private infix fun StudentRepository.findByUser(user: User): Student =
        this.findByUser(user)
            ?: throw StudentNotFoundException("존재하지 않는 학생입니다. info : [ userId = ${user.id} ]")

    private infix fun StudentRepository.findStudentById(studentId: UUID): Student =
        this.findStudentById(studentId)
            ?: throw StudentNotFoundException("존재하지 않는 학생입니다. info : [ studentId = $studentId ]")

    private infix fun CertificationRepository.findAllByStudentIdOrderByAcquisitionDateDesc(studentId: UUID): List<Certification> =
        this.findAllByStudentIdOrderByAcquisitionDateDesc(studentId)

    private infix fun CertificationRepository.findById(id: UUID): Certification =
        this.findByIdOrNull(id)
            ?: throw CertificationNotFoundException("존재하지 않는 자격증입니다. info : [ certificationId = $id ]")

    private infix fun TeacherRepository.findByUser(user: User): Teacher =
        this.findByUser(user)
            ?: throw TeacherNotFoundException("존재하지 않는 선생님입니다. info : [ userId = ${user.id} ]")

    private fun findStudentByUser(user: User) = studentRepository.findByUser(user)
        ?: throw StudentNotFoundException("학생을 찾을 수 없습니다. info : [ userId = ${user.id} ]")

    private fun findTeacherByUser(user: User) = teacherRepository.findByUser(user)
        ?: throw TeacherNotFoundException("취업 동아리 선생님을 찾을 수 없습니다. info : [ userId = ${user.id} ]")

    private fun findBbozzakByUser(user: User) = bbozzakRepository.findByUser(user)
        ?: throw BbozzakNotFoundException("뽀짝 선생님을 찾을 수 없습니다.  info : [ userId = ${user.id} ]")

    private fun findProfessorByUser(user: User) = professorRepository.findByUser(user)
        ?: throw ProfessorNotFoundException("대학 교수를 찾을 수 없습니다. info : [ userId = ${user.id} ]")

    private fun findCompanyInstructorByUser(user: User) = companyInstructorRepository.findByUser(user)
        ?: throw CompanyNotFoundException("기업 강사를 찾을 수 없습니다. info : [ userId = ${user.id} ]")

    private fun findGovernmentByUser(user: User) = governmentRepository.findByUser(user)
        ?: throw GovernmentNotFoundException("유관기관을 찾을 수 없습니다. info : [ userId = ${user.id} ]")
}