package team.msg.domain.student.service

import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.msg.common.enum.ApproveStatus
import team.msg.common.util.UserUtil
import team.msg.domain.bbozzak.repository.BbozzakRepository
import team.msg.domain.company.repository.CompanyInstructorRepository
import team.msg.domain.government.repository.GovernmentRepository
import team.msg.domain.professor.repository.ProfessorRepository
import team.msg.domain.student.event.UpdateStudentActivityEvent
import team.msg.domain.student.exception.ForbiddenStudentActivityException
import team.msg.domain.student.exception.StudentActivityNotFoundException
import team.msg.domain.student.exception.StudentNotFoundException
import team.msg.domain.student.model.StudentActivity
import team.msg.domain.student.presentation.data.request.CreateStudentActivityRequest
import team.msg.domain.student.presentation.data.request.UpdateStudentActivityRequest
import team.msg.domain.student.presentation.data.response.StudentActivityListResponse
import team.msg.domain.student.presentation.data.response.StudentActivityResponse
import team.msg.domain.student.repository.StudentActivityRepository
import team.msg.domain.student.repository.StudentRepository
import team.msg.domain.teacher.exception.TeacherNotFoundException
import team.msg.domain.teacher.repository.TeacherRepository
import team.msg.domain.user.enums.Authority
import team.msg.global.exception.ForbiddenException
import java.util.*

@Service
class StudentActivityServiceImpl(
    private val userUtil: UserUtil,
    private val studentRepository: StudentRepository,
    private val teacherRepository: TeacherRepository,
    private val bbozzakRepository: BbozzakRepository,
    private val professorRepository: ProfessorRepository,
    private val governmentRepository: GovernmentRepository,
    private val companyInstructorRepository: CompanyInstructorRepository,
    private val studentActivityRepository: StudentActivityRepository,
    private val applicationEventPublisher: ApplicationEventPublisher
) : StudentActivityService {

    /**
     * 학생 활동을 생성하는 비지니스 로직입니다.
     * @param 학생 활동을 생성하기 위해 데이터를 담은 request Dto
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun createStudentActivity(request: CreateStudentActivityRequest) {
        val user = userUtil.queryCurrentUser()

        val student = studentRepository.findByUser(user)
            ?: throw StudentNotFoundException("학생을 찾을 수 없습니다. info : [ name = ${user.name} ]")

        val teacher = teacherRepository.findByClub(student.club)
            ?: throw TeacherNotFoundException("취업 동아리 선생님을 찾을 수 없습니다.")

        val studentActivity = StudentActivity(
            id = UUID.randomUUID(),
            title = request.title,
            content = request.content,
            credit = request.credit,
            activityDate = request.activityDate,
            student = student,
            teacher = teacher,
            approveStatus = ApproveStatus.PENDING
        )

        studentActivityRepository.save(studentActivity)
    }


    /**
     * 학생 활동을 업데이트하는 비지니스 로직입니다.
     * applicationEventPublisher로부터 학생 활동 업데이트 이벤트를 발행합니다.
     * @param 학생 활동 id, 학생 활동을 수정하기 위한 데이터들을 담은 request Dto
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun updateStudentActivity(id: UUID, request: UpdateStudentActivityRequest) {
        val user = userUtil.queryCurrentUser()

        val student = studentRepository.findByUser(user)
            ?: throw StudentNotFoundException("학생을 찾을 수 없습니다. info : [ userId = ${user.id}, username = ${user.name} ]")

        val studentActivity = studentActivityRepository.findByIdOrNull(id)
            ?: throw StudentActivityNotFoundException("학생 활동을 찾을 수 없습니다. info : [ studentActivityId = $id ]")

        if(student.id != studentActivity.student.id)
            throw ForbiddenStudentActivityException("해당 학생 활동에 대한 권한이 없습니다. info : [ studentId = ${student.id} ]")

        val updatedStudentActivity = StudentActivity(
            id = studentActivity.id,
            title = request.title,
            content = request.content,
            credit = request.credit,
            activityDate = request.activityDate,
            approveStatus = ApproveStatus.PENDING,
            student = studentActivity.student,
            teacher = studentActivity.teacher
        )
        applicationEventPublisher.publishEvent(UpdateStudentActivityEvent(studentActivity))
        studentActivityRepository.save(updatedStudentActivity)
    }

    /**
     * 학생활동을 삭제하는 비지니스 로직입니다.
     * @param 학생활동을 삭제하기 위한 id
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun deleteStudentActivity(id: UUID) {
        val user = userUtil.queryCurrentUser()

        val student = studentRepository.findByUser(user)
            ?: throw StudentNotFoundException("학생을 찾을 수 없습니다. info : [ userId = ${user.id}, username = ${user.name} ]")

        val studentActivity = studentActivityRepository.findByIdOrNull(id)
            ?: throw StudentActivityNotFoundException("학생 활동을 찾을 수 없습니다. info : [ studentActivityId = $id ]")

        if(student.id != studentActivity.student.id)
            throw ForbiddenStudentActivityException("해당 학생 활동에 대한 권한이 없습니다. info : [ studentId = ${student.id} ]")

        studentActivityRepository.delete(studentActivity)
    }

    /**
     * 학생활동을 거절 및 삭제하는 비지니스 로직입니다.
     * @param 학생활동을 삭제하기 위한 id
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun rejectStudentActivity(id: UUID) {
        val user = userUtil.queryCurrentUser()

        val teacher = teacherRepository.findByUser(user)
            ?: throw TeacherNotFoundException("취업 동아리 선생님을 찾을 수 없습니다. info : [ userId = ${user.id}, username = ${user.name} ]")

        val studentActivity = studentActivityRepository.findByIdOrNull(id)
            ?: throw StudentActivityNotFoundException("학생 활동을 찾을 수 없습니다. info : [ studentActivityId = $id ]")

        if(teacher.id != studentActivity.teacher.id)
            throw ForbiddenStudentActivityException("해당 학생 활동에 대한 권한이 없습니다. info : [ teacherId = ${teacher.id} ]")

        studentActivityRepository.delete(studentActivity)
    }

    /**
     * 학생활동을 승인하는 비즈니스 로직
     * @param 학생활동을 승인하기 위한 id
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun approveStudentActivity(id: UUID) {
        val user = userUtil.queryCurrentUser()

        val teacher = teacherRepository.findByUser(user)
            ?: throw TeacherNotFoundException("취업 동아리 선생님을 찾을 수 없습니다. info : [ userId = ${user.id}, username = ${user.name} ]")

        val studentActivity = studentActivityRepository.findByIdOrNull(id)
            ?: throw StudentActivityNotFoundException("학생 활동을 찾을 수 없습니다. info : [ studentActivityId = $id ]")

        if(teacher.id != studentActivity.teacher.id)
            throw ForbiddenStudentActivityException("해당 학생 활동에 대한 권한이 없습니다. info : [ teacherId = ${teacher.id} ]")

        val updatedStudentActivity = StudentActivity(
            id = studentActivity.id,
            title = studentActivity.title,
            content = studentActivity.content,
            credit = studentActivity.credit,
            activityDate = studentActivity.activityDate,
            approveStatus = ApproveStatus.APPROVED,
            student = studentActivity.student,
            teacher = studentActivity.teacher
        )

        studentActivityRepository.save(updatedStudentActivity)
    }

    /**
     * 학생활동을 전체 조회하는 비즈니스 로직
     * @param 학생활동을 페이징 처리하기 위한 pageable
     */
    @Transactional(readOnly = true)
    override fun queryAllStudentActivity(pageable: Pageable): StudentActivityListResponse {
        val user = userUtil.queryCurrentUser()

        val studentActivities = studentActivityRepository.findAll(pageable)

        val response = StudentActivityListResponse(
            studentActivities.map {
                StudentActivityResponse.of(it, user)
            }
        )
        return response
    }

    /**
     * 학생활동을 학생 단위로 조회하는 비즈니스 로직
     * @param 학생활동을 조회하기 위한 학생 id 및 페이징을 처리하기 위한 pageable
     */
    @Transactional(readOnly = true)
    override fun queryStudentActivityByStudent(studentId: UUID, pageable: Pageable): StudentActivityListResponse {
        val user = userUtil.queryCurrentUser()

        val student = studentRepository.findByIdOrNull(studentId)
            ?: throw StudentNotFoundException("학생을 찾을 수 없습니다. info : [ studentId = $studentId ]")

        if(!when(user.authority) {
                Authority.ROLE_TEACHER -> teacherRepository.existsByClubAndUser(student.club, user)
                Authority.ROLE_BBOZZAK -> bbozzakRepository.existsByClubAndUser(student.club, user)
                Authority.ROLE_PROFESSOR -> professorRepository.existsByClubAndUser(student.club, user)
                Authority.ROLE_COMPANY_INSTRUCTOR -> companyInstructorRepository.existsByClubAndUser(student.club, user)
                Authority.ROLE_GOVERNMENT -> governmentRepository.existsByClubAndUser(student.club, user)
                else -> throw ForbiddenException("접근 권한이 없는 역할입니다. info : [ authority = ${user.authority} ]")
            })
            throw ForbiddenStudentActivityException("해당 학생의 학생 활동에 대한 접근 권한이 없습니다. info : [ userId = ${user.id}, studentId = $studentId ]")

        val studentActivities = studentActivityRepository.findAllByStudent(student, pageable)

        val response = StudentActivityListResponse(
            studentActivities.map {
                StudentActivityResponse.of(it, user)
            }
        )

        return response
    }
}