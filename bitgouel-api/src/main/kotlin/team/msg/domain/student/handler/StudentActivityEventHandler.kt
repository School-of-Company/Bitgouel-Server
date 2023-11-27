package team.msg.domain.student.handler

import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import team.msg.common.enums.ApproveStatus
import team.msg.domain.student.event.UpdateStudentActivityEvent
import team.msg.domain.student.model.StudentActivityHistory
import team.msg.domain.student.repository.StudentActivityHistoryRepository
import java.util.*

@Component
class StudentActivityEventHandler(
    private val studentActivityHistoryRepository: StudentActivityHistoryRepository
) {

    /**
     * studentActivity의 update 이벤트가 발행되면 히스토리를 저장하는 핸들러입니다.
     * @param studentActivity update 이벤트
     */
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    fun updateStudentActivityHandler(event: UpdateStudentActivityEvent) {
        val studentActivity = event.studentActivity
        val studentActivityHistory = StudentActivityHistory(
            id = UUID.randomUUID(),
            title = studentActivity.title,
            content = studentActivity.content,
            credit = studentActivity.credit,
            approveStatus = ApproveStatus.APPROVED,
            activityDate = studentActivity.activityDate,
            student = studentActivity.student,
            teacher = studentActivity.teacher,
            studentActivityId = studentActivity.id
        )
        studentActivityHistoryRepository.save(studentActivityHistory)
    }
}