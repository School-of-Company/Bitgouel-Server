package team.msg.domain.lecture.repository

import org.springframework.data.repository.CrudRepository
import team.msg.domain.lecture.model.LectureDate
import java.util.*

interface LectureDateRepository : CrudRepository<LectureDate,UUID>