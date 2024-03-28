package team.msg.domain.lecture.repository

import javax.persistence.LockModeType
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.repository.CrudRepository
import team.msg.domain.lecture.model.Lecture
import team.msg.domain.lecture.model.RegisteredLectureCount
import java.util.UUID

interface RegisteredLectureCountRepository : CrudRepository<RegisteredLectureCount, UUID> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findByLecture(lecture: Lecture): RegisteredLectureCount?
}