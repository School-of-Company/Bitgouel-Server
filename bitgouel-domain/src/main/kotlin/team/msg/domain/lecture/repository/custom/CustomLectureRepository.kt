package team.msg.domain.lecture.repository.custom

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import team.msg.common.enums.ApproveStatus
import team.msg.domain.lecture.enums.LectureType
import team.msg.domain.lecture.model.Lecture
import java.util.UUID

interface CustomLectureRepository {
    fun findAllByApproveStatusAndLectureType(pageable: Pageable, approveStatus: ApproveStatus?, lectureType: LectureType?): Page<Lecture>
    fun deleteAllByUserId(userId: UUID)
}