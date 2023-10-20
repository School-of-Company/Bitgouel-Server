package team.msg.domain.student.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.msg.common.enum.ApproveStatus
import team.msg.common.util.UserUtil
import team.msg.domain.student.exception.StudentNotFoundException
import team.msg.domain.student.model.StudentActivity
import team.msg.domain.student.presentation.data.request.CreateStudentActivityRequest
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
     * @param CreateStudentActivityRequest
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun createStudentActivity(request: CreateStudentActivityRequest) {
        val user = userUtil.queryCurrentUser()

        val student = studentRepository.findByUser(user) ?: throw StudentNotFoundException("학생을 찾을 수 없습니다.")

        val teacher = teacherRepository.findByClub(student.club) ?: throw TeacherNotFoundException("취업 동아리 선생님을 찾을 수 없습니다.")

        val studentActivity = StudentActivity(
            id = UUID.randomUUID(),
            title = request.title,
            content = request.content,
            credit = request.credit,
            createdAt = request.createdAt,
            student = student,
            teacher = teacher,
            approveStatus = ApproveStatus.PENDING
        )

        studentActivityRepository.save(studentActivity)
    }
}