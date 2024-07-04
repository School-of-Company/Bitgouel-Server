package team.msg.domain.lecture.repository

import org.springframework.data.repository.CrudRepository
import team.msg.domain.lecture.model.LectureLocation
import java.util.UUID

interface LectureLocationRepository : CrudRepository<LectureLocation, Long> {
    fun findByLectureId(lectureId: UUID): LectureLocation
}
