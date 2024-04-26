package team.msg.domain.lecture.repository

import org.springframework.data.repository.CrudRepository
import team.msg.domain.lecture.model.Lecture
import team.msg.domain.lecture.model.LectureDate
import team.msg.domain.lecture.repository.custom.CustomLectureDateRepository
import java.util.*

interface LectureDateRepository : CrudRepository<LectureDate,UUID>, CustomLectureDateRepository {
    fun findAllByLecture(lecture: Lecture): List<LectureDate>
}