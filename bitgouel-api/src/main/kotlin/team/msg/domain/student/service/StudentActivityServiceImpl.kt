package team.msg.domain.student.service

import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.msg.common.ulid.ULIDGenerator
import team.msg.common.util.UserUtil
import team.msg.domain.bbozzak.model.Bbozzak
import team.msg.domain.club.model.Club
import team.msg.domain.company.model.CompanyInstructor
import team.msg.domain.government.model.GovernmentInstructor
import team.msg.domain.university.model.Professor
import team.msg.domain.student.exception.ForbiddenStudentActivityException
import team.msg.domain.student.exception.StudentActivityNotFoundException
import team.msg.domain.student.exception.StudentNotFoundException
import team.msg.domain.student.model.Student
import team.msg.domain.student.model.StudentActivity
import team.msg.domain.student.presentation.data.request.CreateStudentActivityRequest
import team.msg.domain.student.presentation.data.request.UpdateStudentActivityRequest
import team.msg.domain.student.presentation.data.response.StudentActivitiesResponse
import team.msg.domain.student.presentation.data.response.StudentActivityDetailsResponse
import team.msg.domain.student.presentation.data.response.StudentActivityResponse
import team.msg.domain.student.repository.StudentActivityRepository
import team.msg.domain.student.repository.StudentRepository
import team.msg.domain.teacher.exception.TeacherNotFoundException
import team.msg.domain.teacher.model.Teacher
import team.msg.domain.teacher.repository.TeacherRepository
import team.msg.domain.user.enums.Authority
import team.msg.domain.user.model.User
import team.msg.global.exception.ForbiddenException
import java.util.*

@Service
class StudentActivityServiceImpl(
    private val userUtil: UserUtil,
    private val studentRepository: StudentRepository,
    private val teacherRepository: TeacherRepository,
    private val studentActivityRepository: StudentActivityRepository
) : StudentActivityService {

    /**
     * 학생 활동을 생성하는 비지니스 로직입니다.
     * @param 학생 활동을 생성하기 위해 데이터를 담은 request Dto
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun createStudentActivity(request: CreateStudentActivityRequest) {
        val user = userUtil.queryCurrentUser()

        val student = studentRepository findByUser user

        val teacher = teacherRepository findByClub student.club

        val studentActivity = StudentActivity(
            id = UUID(0, 0),
            ulid = ULIDGenerator.generateULID(),
            title = request.title,
            content = request.content,
            credit = request.credit,
            activityDate = request.activityDate,
            student = student,
            teacher = teacher
        )

        studentActivityRepository.save(studentActivity)
    }


    /**
     * 학생 활동을 업데이트하는 비지니스 로직입니다.
     * @param 학생 활동 id, 학생 활동을 수정하기 위한 데이터들을 담은 request Dto
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun updateStudentActivity(id: UUID, request: UpdateStudentActivityRequest) {
        val user = userUtil.queryCurrentUser()

        val student = studentRepository findByUser user

        val studentActivity = studentActivityRepository findById id

        if(student.id != studentActivity.student.id)
            throw ForbiddenStudentActivityException("해당 학생 활동에 대한 권한이 없습니다. info : [ studentId = ${student.id} ]")

        val updatedStudentActivity = StudentActivity(
            id = studentActivity.id,
            ulid = studentActivity.ulid,
            title = request.title,
            content = request.content,
            credit = request.credit,
            activityDate = request.activityDate,
            student = studentActivity.student,
            teacher = studentActivity.teacher
        )

        studentActivityRepository.save(updatedStudentActivity)
    }

    /**
     * 학생활동을 삭제하는 비지니스 로직입니다.
     * @param 학생활동을 삭제하기 위한 id
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun deleteStudentActivity(id: UUID) {
        val user = userUtil.queryCurrentUser()

        val student = studentRepository findByUser user

        val studentActivity = studentActivityRepository findById id

        if(student != studentActivity.student)
            throw ForbiddenStudentActivityException("해당 학생 활동에 대한 권한이 없습니다. info : [ studentId = ${student.id} ]")

        studentActivityRepository.delete(studentActivity)
    }

    /**
     * 학생활동을 전체 조회하는 비즈니스 로직
     * @param 학생활동을 페이징 처리하기 위한 pageable
     */
    @Transactional(readOnly = true)
    override fun queryAllStudentActivities(pageable: Pageable): StudentActivitiesResponse {
        val user = userUtil.queryCurrentUser()

        val studentActivities = studentActivityRepository.findAll(pageable)

        val response = StudentActivitiesResponse(
            StudentActivityResponse.pageOf(studentActivities, user)
        )
        
        return response
    }

    /**
     * 학생활동을 학생 단위로 조회하는 비즈니스 로직
     * @param 학생활동을 조회하기 위한 학생 id 및 페이징을 처리하기 위한 pageable
     */
    @Transactional(readOnly = true)
    override fun queryStudentActivitiesByStudent(studentId: UUID, pageable: Pageable): StudentActivitiesResponse {
        val user = userUtil.queryCurrentUser()

        val student = studentRepository findById studentId

        when(user.authority) {
            Authority.ROLE_ADMIN -> {}
            Authority.ROLE_TEACHER -> {
                val teacher = teacherRepository findByUser user

                if(student.club != teacher.club)
                    throw ForbiddenStudentActivityException("해당 학생 활동에 대한 권한이 없습니다. info : [ teacherId = ${teacher.id} ]")
            }
            else -> throw ForbiddenException("Forbidden")
        }

        val studentActivities = studentActivityRepository.findAllByStudent(student, pageable)

        val response = StudentActivitiesResponse(
            StudentActivityResponse.pageOf(studentActivities, student.user!!)
        )

        return response
    }

    /**
     * 학생 자신의 학생활동을 조회하는 비즈니스 로직
     * @param 페이징을 처리하기 위한 pageable
     */
    @Transactional(readOnly = true)
    override fun queryMyStudentActivities(pageable: Pageable): StudentActivitiesResponse {
        val user = userUtil.queryCurrentUser()

        val student = studentRepository findByUser user

        val studentActivities = studentActivityRepository.findAllByStudent(student, pageable)

        val response = StudentActivitiesResponse(
            StudentActivityResponse.pageOf(studentActivities, user)
        )

        return response
    }

    /**
     * 학생 활동의 상세 정보를 조회하는 비즈니스 로직
     * @param 학생 활동을 조회하기 위한 id
     */
    @Transactional(readOnly = true)
    override fun queryStudentActivityDetail(id: UUID): StudentActivityDetailsResponse {
        val user = userUtil.queryCurrentUser()

        val entity = userUtil.getAuthorityEntityAndOrganization(user).first

        val studentActivity = studentActivityRepository findById id

        when(entity) {
            is Student -> {
                if(entity != studentActivity.student)
                    throw ForbiddenStudentActivityException("해당 학생 활동에 대한 권한이 없습니다. info : [ userId = ${user.id} ]")
            }
            is Teacher -> {
                if(entity != studentActivity.teacher)
                    throw ForbiddenStudentActivityException("해당 학생 활동에 대한 권한이 없습니다. info : [ userId = ${user.id} ]")
            }
            is Bbozzak -> {
                if(entity.club != studentActivity.student.club)
                    throw ForbiddenStudentActivityException("해당 학생 활동에 대한 권한이 없습니다. info : [ userId = ${user.id} ]")
            }
            is Professor, 
            is CompanyInstructor, 
            is GovernmentInstructor ->  throw ForbiddenStudentActivityException("유효하지 않은 권한입니다. info : [ userAuthority = ${user.authority} ]")
        }
        
        val response = StudentActivityResponse.detailOf(studentActivity)

        return response
    }

    private infix fun StudentRepository.findByUser(user: User): Student =
        this.findByUser(user) ?: throw StudentNotFoundException("학생을 찾을 수 없습니다. info : [ userId = ${user.id} ]")

    private infix fun StudentRepository.findById(id: UUID): Student =
        this.findByIdOrNull(id) ?: throw throw StudentNotFoundException("학생을 찾을 수 없습니다. info : [ userId = $id ]")

    private infix fun StudentActivityRepository.findById(id: UUID): StudentActivity =
        this.findByIdOrNull(id) ?: throw StudentActivityNotFoundException("학생 활동을 찾을 수 없습니다. info : [ studentActivityId = $id ]")

    private infix fun TeacherRepository.findByUser(user: User): Teacher =
        this.findByUser(user) ?: throw TeacherNotFoundException("취업 동아리 선생님을 찾을 수 없습니다. info : [ userId = ${user.id} ]")

    private infix fun TeacherRepository.findByClub(club: Club): Teacher =
        this.findByClub(club) ?: throw TeacherNotFoundException("관련 동아리의 취업 동아리 선생님을 찾을 수 없습니다. info : [ clubId = ${club.id} ]")

}