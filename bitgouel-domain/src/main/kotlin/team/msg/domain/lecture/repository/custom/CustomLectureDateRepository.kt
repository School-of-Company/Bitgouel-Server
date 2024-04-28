package team.msg.domain.lecture.repository.custom

import java.time.LocalDate
import java.util.UUID

interface CustomLectureDateRepository {
    fun findByCurrentCompletedDate(lectureId: UUID): LocalDate?
}