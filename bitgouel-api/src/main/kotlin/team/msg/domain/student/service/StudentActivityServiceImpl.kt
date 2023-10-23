package team.msg.domain.student.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.msg.common.enum.ApproveStatus
import team.msg.common.util.UserUtil
import team.msg.domain.student.exception.StudentActivityNotFoundException
import team.msg.domain.student.exception.StudentNotFoundException
import team.msg.domain.student.model.StudentActivity
import team.msg.domain.student.presentation.data.request.CreateStudentActivityRequest
import team.msg.domain.student.presentation.data.request.UpdateStudentActivityRequest
import team.msg.domain.student.repository.StudentActivityRepository
import team.msg.domain.student.repository.StudentRepository
import team.msg.domain.teacher.exception.TeacherNotFoundException
import team.msg.domain.teacher.repository.TeacherRepository
import java.util.*

@Service
class StudentActivityServiceImpl(
    private val userUtil: UserUtil,
    private val studentRepository: StudentRepository,
    private val teacherRepository: TeacherRepository,
    private val studentActivityRepository: StudentActivityRepository
) : StudentActivityService {

    /**
     * 학생 활동을 생성하는 비지니스 로직입니다
     * @param 학생활동을 생성하기 위한 request dto 입니다.
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun createStudentActivity(request: CreateStudentActivityRequest) {
        val user = userUtil.queryCurrentUser()

        val student = studentRepository.findByUser(user)
            ?: throw StudentNotFoundException("학생을 찾을 수 없습니다. info : [ name = ${user.name} ]")

        val teacher = teacherRepository.findByClub(student.club)
            ?: throw TeacherNotFoundException("취업 동아리 선생님을 찾을 수 없습니다.")

        val studentActivity = StudentActivity(
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
     * 학생 활동을 업데이트하는 비지니스 로직입니다
     * @param 학생 활동을 업데이트하기 위한 id와 request dto 입니다.
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun updateStudentActivity(id: UUID, request: UpdateStudentActivityRequest) {
        val user = userUtil.queryCurrentUser()

        val student = studentRepository.findByUser(user)
            ?: throw StudentNotFoundException("학생을 찾을 수 없습니다. info : [ userId = ${user.id}, username = ${user.name} ]")

        val studentActivity = studentActivityRepository.findByIdAndStudent(id, student)
            ?: throw StudentActivityNotFoundException("학생 활동을 찾을 수 없습니다. info : [ studentActivityId = $id ]")

        val updatedStudentActivity = studentActivity.updateStudentActivity(
            title = request.title,
            content = request.content,
            credit = request.credit,
            activityDate = request.activityDate
        )

        studentActivityRepository.save(updatedStudentActivity)
    }

    /**
     * 학생활동을 삭제하는 비지니스 로직입니다.
     * @param 학생활동을 삭제하기 위한 id 입니다.
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun deleteStudentActivity(id: UUID) {
        val user = userUtil.queryCurrentUser()

        val student = studentRepository.findByUser(user)
            ?: throw StudentNotFoundException("학생을 찾을 수 없습니다. info : [ userId = ${user.id}, username = ${user.name} ]")

        val studentActivity = studentActivityRepository.findByIdAndStudent(id, student)
            ?: throw StudentActivityNotFoundException("학생 활동을 찾을 수 없습니다. info : [ studentActivityId = $id ]")

        studentActivityRepository.delete(studentActivity)
    }
}